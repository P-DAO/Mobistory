package fr.uge.mobistory.main

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.uge.mobistory.AppBar
import fr.uge.mobistory.ImportEventTxt
import fr.uge.mobistory.R
import fr.uge.mobistory.affichage.*
import fr.uge.mobistory.database.EventRepository
import fr.uge.mobistory.database.HistoricalEventAndClaim
import fr.uge.mobistory.tri.SortType
import fr.uge.mobistory.ui.theme.MobistoryTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var eventRepository: EventRepository

    private val eventsState = mutableStateOf(emptyList<HistoricalEventAndClaim>())

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var sortType by remember { mutableStateOf(SortType.DATE) }
            var expanded by remember { mutableStateOf(true) }
            var selectedMenuItem: String? by remember { mutableStateOf(null) }

            MobistoryTheme {
                val scaffoldState = rememberScaffoldState()
                val scope = rememberCoroutineScope()
                Scaffold(
                    scaffoldState = scaffoldState,
                    topBar = {
                        AppBar(
                            onNavigationIconClick = {
                                scope.launch {
                                    scaffoldState.drawerState.open()
                                }
                            }
                        )
                    },
                    drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
                    drawerContent = {
                        DrawerHeader()
                        DrawerBody(
                            items = listOf(
                                MenuItem(
                                    id = "accueil",
                                    title = "Accueil",
                                    contentDescription = "Direction l'accueil",
                                    icon = Icons.Default.Home
                                ),
                                MenuItem(
                                    id = "tri",
                                    title = "Tri",
                                    contentDescription = "Tri par date, nom de l'évènement, localisation ou popularité",
                                    icon = Icons.Default.List,
                                    content = {
                                        Box(
                                            modifier = Modifier.clickable { expanded = !expanded }
                                        ){
                                            DropDownMenu(onSortTypeSelected = { selectedSortType ->
                                                sortType = selectedSortType
                                                expanded = false
                                            })
                                        }}
                                ),
                                MenuItem(
                                    id = "favori",
                                    title = "Favoris",
                                    contentDescription = "Évènements favoris",
                                    icon = Icons.Default.Star
                                )
                            ),onItemClick = {
                                println("Clicked on ${it.title}")
                            }
                        )
                    }
                ) {
                    eventRepository = EventRepository(applicationContext)

                    val viewModel: ImportEventTxt by viewModels()

                    // Lecture du fichier de ressource brute
                    val fileInputStream = resources.openRawResource(R.raw.events)
                    val fileContent: List<String> = fileInputStream.bufferedReader().readLines()

                    viewModel.importEventTxtInDataBase(fileContent, eventRepository)

                    val coroutineScope = rememberCoroutineScope()

                    coroutineScope.launch {
                        val allEvents = eventRepository.getHistoricalEventWithClaimsAll()
                        eventsState.value = allEvents
                    }
//                    var value = eventsState.value.getOrNull(3)
//                    if(value != null){
//                        displayEvent(value)
//                    }
//                    displayAllEvents(eventsState.value, eventRepository, coroutineScope)
                    displayAllEvents(eventRepository, sortType)

//                    DropDownMenu() {
//                        sortType = it
//                    }
                }
            }
        }
    }

}