package fr.uge.mobistory

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.room.Room
import fr.uge.mobistory.database.EventDatabase
import fr.uge.mobistory.ui.theme.MobistoryTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

class MainActivity : ComponentActivity() {

    private lateinit var eventDatabase: EventDatabase // facilite acces pour diff parties de la classe

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobistoryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    // Création de la base de données
                    eventDatabase = Room.databaseBuilder(
                        applicationContext,
                        EventDatabase::class.java,
                        "event_database"
                    ).build()
                    GlobalScope.launch {
                        // Lecture du fichier de ressource brute
                        val fileInputStream = resources.openRawResource(R.raw.events)
                        val fileContent = fileInputStream.bufferedReader().use { it.readText() }

                        // Désérialisation des données JSON en objets HistoricalEvent
                        val historicalEvents = Json.decodeFromString<List<HistoricalEvent>>(fileContent)

                        // Importer les événements dans la base de données
                        eventDatabase.historicalEventDao().upsertEvents(historicalEvents)

                        // Récupérer tous les événements depuis la base de données
                        val allEvents = eventDatabase.historicalEventDao().getAll()

                        // Afficher les événements dans la console
                        for (event in allEvents) {
                            println("Label: ${event.label}")
                            println("Description: ${event.description}")
//                            println("Date: ${event.date}")
                            println("Popularity: ${event.popularity}")
                            println("Claims: ${event.claims}")
                            println()
                        }
                    }

//                    // chemin du fichier .txt contenant les données JSON
//                    val jsonString = "app/res/raw/events.txt"
//
//                    // Lecture du fichier
//                    val fileContent = File(jsonString).readText()
//
//                    // désérialisation des données JSON en obj HistoricalEvent
//                    val historicalEvent = Json.decodeFromString<List<HistoricalEvent>>(fileContent)
//
//                    // Import des événements dans la base de données
//                    val eventImporter = ImportFile(eventDatabase)
//                    GlobalScope.launch {
//                        eventImporter.importEvents(historicalEvent)
//                        // Les événements ont été importés avec succès
//
//                        // parcourir les events et affichage des infos
//                        for (event in historicalEvent) {
//                            println("Label: ${event.label}")
////                        println("Date: ${event.date}")
//                            println("Description: ${event.description}")
//                        }
//                    }
                }
            }
        }
    }

}