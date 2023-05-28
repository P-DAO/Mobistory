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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import fr.uge.mobistory.ui.theme.MobistoryTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
//                    CoroutineScope(Dispatchers.IO).launch {
//                        val eventImport = ImportFile()
//                        val database = Room.databaseBuilder(
//                            applicationContext,
//                            EventDatabase::class.java,
//                            "event_database"
//                        ).build()
//
//                        eventImport.importEventsFromFile(this@MainActivity, database)
//                    }
                    // Création de la base de données
                    eventDatabase = Room.databaseBuilder(
                        applicationContext,
                        EventDatabase::class.java,
                        "event_database"
                    ).build()

                    // chemin du fichier .txt contenant les données JSON
                    val jsonString = "app/res/raw/events.txt"

                    // Lecture du fichier
                    val fileContent = File(jsonString).readText()

                    // désérialisation des données JSON en obj HistoricalEvent
                    val historicalEvent = Json.decodeFromString<List<HistoricalEvent>>(fileContent)

                    // Import des événements dans la base de données
                    val eventImporter = ImportFile(eventDatabase)
                    GlobalScope.launch {
                        eventImporter.importEvents(historicalEvent)
                        // Les événements ont été importés avec succès

                        // parcourir les events et affichage des infos
                        for (event in historicalEvent) {
                            println("Label: ${event.label}")
//                        println("Date: ${event.date}")
                            println("Description: ${event.description}")
                        }
                    }
                }
            }
        }
    }

}