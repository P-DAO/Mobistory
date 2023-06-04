package fr.uge.mobistory.affichage

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import fr.uge.mobistory.R
import fr.uge.mobistory.database.EventRepository
import fr.uge.mobistory.database.HistoricalEventAndClaim
import fr.uge.mobistory.menu.DropDownMenu
import fr.uge.mobistory.menu.EventList
import fr.uge.mobistory.menu.SearchMenuState
import fr.uge.mobistory.quiz.Quiz
import fr.uge.mobistory.timeline.TimelineDisplayer
import fr.uge.mobistory.tri.SortType
import fr.uge.mobistory.utils.extractLabel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Locale

enum class DisplayState {
    HOME,
    EVENT,
    EVENTS,
    TIMELINE,
    QUIZ
}

data class DrawerMenuItem(val icon: ImageVector, val label: String)

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainDisplayer(eventRepository: EventRepository) {

    var displayState by rememberSaveable { mutableStateOf(DisplayState.HOME) }
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    var sortType by rememberSaveable { mutableStateOf(SortType.DATE) }
    var events: List<HistoricalEventAndClaim> by remember { mutableStateOf(listOf()) }
    var event by rememberSaveable { mutableStateOf("") }
    var searchState by rememberSaveable { mutableStateOf(SearchMenuState.OPEN) }

    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(Unit) {
        events = eventRepository.getHistoricalEventWithClaimsAll()
    }

    val listMenu = listOf(
        DrawerMenuItem(icon = Icons.Rounded.Home, label = "Home"),
        DrawerMenuItem(icon = Icons.Rounded.List, label = "Events"),//TODO on a perdu le titre
        DrawerMenuItem(icon = Icons.Rounded.Search, label = "Event"),
        DrawerMenuItem(icon = Icons.Rounded.Star, label = "Favoris"),
        DrawerMenuItem(icon = ImageVector.vectorResource(id = R.drawable.baseline_question_answer_24), label = "Quiz"),
        DrawerMenuItem(icon = ImageVector.vectorResource(id = R.drawable.frise_chrono_icon_24), label = "Timeline")
    )

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = "Mobistory") },
                navigationIcon = { IconButton(onClick = {
                    coroutineScope.launch { scaffoldState.drawerState.open() }
                }) {
                    Icon(imageVector = Icons.Rounded.Menu, contentDescription = "Drawer Icon")
                } },
                actions = {
                    when (displayState) {
                        DisplayState.EVENT -> {
                            Column {
                                SearchView(state = textState, searchState = { searchState = it }) }
                            }
                        DisplayState.EVENTS -> { DropDownMenu { newSortType -> sortType = newSortType } }
                        else -> {}
                    }
                }
            )
        },
        drawerContent = {
            DisplayDrawer(
                listMenu = listMenu,
                state = { newState -> displayState = newState },
                coroutineScope = coroutineScope,
                scaffoldState = scaffoldState
            )
        },
        drawerGesturesEnabled = true,
    ) {
        when (displayState) {
            DisplayState.EVENTS -> { displayAllEvents(eventRepository, sortType, true) }
            DisplayState.EVENT -> {
                when (searchState) {
                    SearchMenuState.OPEN -> {
                        EventList(listEvent = events, state = textState, event = {newEvent -> event = newEvent}, searchState = {searchState = it})
                    }
                    SearchMenuState.CLOSE -> {
                        val newEvent = events.first { events -> event == extractLabel(events) }
                        displayEvent(event = newEvent)
                    }
                }
            }
            DisplayState.HOME -> { Text("évènement du jour a faire") }
            DisplayState.TIMELINE -> { TimelineDisplayer(events = events) }
            DisplayState.QUIZ -> { Quiz(events = events) }
        }
    }
}

@Composable
fun DisplayDrawer(listMenu: List<DrawerMenuItem>, state: (DisplayState) -> Unit, coroutineScope: CoroutineScope, scaffoldState: ScaffoldState) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier
            .size(126.dp)
            .clip(CircleShape), contentAlignment = Alignment.Center) {
            Image(modifier = Modifier.matchParentSize(), painter = painterResource(id = R.drawable.ic_launcher_background), contentDescription = "")
            Image(modifier = Modifier.scale(1.4f), painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "",)
        }
        Spacer(modifier = Modifier.height(24.dp))

        listMenu.forEach { item ->
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .clickable {
                    coroutineScope.launch { scaffoldState.drawerState.close() }
                    when (item.label) {
                        "Events" -> {
                            state(DisplayState.EVENTS)
                        }

                        "Event" -> {
                            state(DisplayState.EVENT)
                        }

                        "Quiz" -> {
                            state(DisplayState.QUIZ)
                        }

                        "Timeline" -> {
                            state(DisplayState.TIMELINE)
                        }

                        else -> {
                            state(DisplayState.HOME)
                        }
                    }
                }) {
                Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start)
                {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = "${item.label} Icon"
                    )
                    Text(modifier = Modifier.padding(start = 24.dp), text = item.label)
                }
            }
        }
    }
}

@Composable
fun SearchView(state: MutableState<TextFieldValue>, searchState: (SearchMenuState) -> Unit) {
    Box(
        Modifier
            .fillMaxHeight()
            .width(150.dp)
    ) {
        TextField(
            value = state.value,
            onValueChange = { value ->
                state.value = value
                searchState(SearchMenuState.OPEN) },
            leadingIcon = { Icon(imageVector = Icons.Rounded.Search, contentDescription = "searchEvent") },
            singleLine = true,
            shape = RectangleShape,
            colors = TextFieldDefaults.textFieldColors( leadingIconColor = Color.White, textColor = Color.White, cursorColor = Color.White),
        )
    }
}




