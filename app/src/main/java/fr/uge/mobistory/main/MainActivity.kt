package fr.uge.mobistory.main

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.room.Room
import fr.uge.mobistory.ImportEventTxt
import fr.uge.mobistory.R
import fr.uge.mobistory.database.EventDatabase
import fr.uge.mobistory.database.EventRepository
import fr.uge.mobistory.ui.theme.MobistoryTheme

class MainActivity : ComponentActivity() {

    private lateinit var eventRepository: EventRepository

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
//                    eventDatabase = Room.databaseBuilder(
//                        applicationContext,
//                        EventDatabase::class.java,
//                        "event_database"
//                    ).build()
                    eventRepository = EventRepository(applicationContext)

                    val viewModel: ImportEventTxt by viewModels()

                    // Lecture du fichier de ressource brute
                    val fileInputStream = resources.openRawResource(R.raw.events)
                    val fileContent: List<String> = fileInputStream.bufferedReader().readLines()

                    viewModel.importEventTxtInDataBase(fileContent, eventRepository)
                }
            }
        }
    }

}