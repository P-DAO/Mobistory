package fr.uge.mobistory.main

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
            MobistoryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    eventRepository = EventRepository(applicationContext)

                    val viewModel: ImportEventTxt by viewModels()

                    // Lecture du fichier de ressource brute
                    val fileInputStream = resources.openRawResource(R.raw.events)
                    val fileContent: List<String> = fileInputStream.bufferedReader().readLines()
//                        .useLines { lineSeq ->
//                        lineSeq.forEach {  }
//                    } //TODO séquence de line

                    viewModel.importEventTxtInDataBase(fileContent, eventRepository)

                    val coroutineScope = rememberCoroutineScope()

                    coroutineScope.launch {
                        val allEvents = eventRepository.getHistoricalEventWithClaimsAll()
                        eventsState.value = allEvents
                    }
                    MainDisplayer(eventRepository = eventRepository)
//                    displayAllEvents(eventRepository, sortType, showFavoritesOnly)

                }
            }
        }
    }

}