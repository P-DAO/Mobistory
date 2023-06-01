package fr.uge.mobistory.main

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import fr.uge.mobistory.AppBar
import fr.uge.mobistory.ImportEventTxt
import fr.uge.mobistory.R
import fr.uge.mobistory.affichage.*
import fr.uge.mobistory.database.EventRepository
import fr.uge.mobistory.database.HistoricalEventAndClaim
import fr.uge.mobistory.menu.DrawerBody
import fr.uge.mobistory.menu.DrawerHeader
import fr.uge.mobistory.menu.MenuItem
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
            var showFavoritesOnly by remember { mutableStateOf(false) }

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
                                    icon = Icons.Default.List
//                                    content = {
//                                        Box(
//                                            modifier = Modifier.clickable { expanded = !expanded }
//                                        ){
//                                            DropDownMenu(onSortTypeSelected = { selectedSortType ->
//                                                sortType = selectedSortType
//                                                expanded = false
//                                            })
//                                        }}
                                ),
                                MenuItem(
                                    id = "favori",
                                    title = "Favoris",
                                    contentDescription = "Évènements favoris",
                                    icon = Icons.Default.Star
                                )
                            ), onItemClick = { item ->
                                when (item.id) {
                                    "accueil" -> {
                                        // Action pour l'élément "Accueil"
                                    }
                                    "tri" -> {
                                        // Action pour l'élément "Tri"
                                        scope.launch {
                                            scaffoldState.drawerState.close() // Fermer le tiroir (drawer)
                                            expanded = !expanded
                                        }
                                    }
                                    "favori" -> {
                                        // Action pour l'élément "Favoris"
                                        scope.launch {
                                            scaffoldState.drawerState.close() // Fermer le tiroir (drawer)
                                            showFavoritesOnly = true // Afficher seulement les favoris
                                        }
                                    }
                                }
                            },
                            onExpandMenu = {
                                expanded = !expanded
                            },
                            expanded = expanded,
                            onSortTypeSelected = { selectedSortType ->
                                // Action lorsque le tri est sélectionné
                                sortType = selectedSortType
                                expanded = false // Réduire le sous-menu de tri
                            },
                            sortType = sortType
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

//                    displayAllEvents(eventRepository, SortType.DATE)
                    MainDisplayer(eventRepository = eventRepository)

                    displayAllEvents(eventRepository, sortType, showFavoritesOnly)

//                    DropDownMenu() {
//                        sortType = it
//                    }

                }
            }
        }
    }

}