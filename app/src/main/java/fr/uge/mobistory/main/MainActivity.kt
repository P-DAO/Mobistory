package fr.uge.mobistory.main

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.room.Room
import fr.uge.mobistory.R
import fr.uge.mobistory.database.EventDatabase
import fr.uge.mobistory.historicalEvent.HistoricalEventEntity
import fr.uge.mobistory.historicalEvent.HistoricalEvent
import fr.uge.mobistory.ui.theme.MobistoryTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.streams.toList

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
                        val fileContent: List<String> = fileInputStream.bufferedReader().readLines()
                        val listHistoricalEvent: List<HistoricalEvent> = fileContent.stream()
                            .map { line -> Json{ignoreUnknownKeys = true}.decodeFromString<HistoricalEvent>(line) }
//                            .peek { event -> Log.d("event", event.toString()) }
                            .toList()

                        // Désérialisation des données JSON en objets HistoricalEvent
                        val historicalEventEntities:List<HistoricalEventEntity> = listHistoricalEvent.stream()
                            .map { event -> event.toHistoricalEventEntity() }
//                            .peek { event -> Log.d("Event_ENTITY", event.toString()) }
                            .toList()

                        listHistoricalEvent.forEach{ event -> event.claims.forEach{
                            val claimEntity = it.toClaimEntity(event.id)
//                            Log.d("ClaimEntity", claimEntity.toString())
                            eventDatabase.claimDao().insertClaim(claimEntity)
                        } }

                        // Importer les événements dans la base de données
                        eventDatabase.historicalEventDao().insertEvents(historicalEventEntities)

                        // Récupérer tous les événements depuis la base de données
                        val allEvents = eventDatabase.historicalEventDao().getHistoricalEventWithClaims()

                        // Afficher les événements dans la console
                        for (event in allEvents) {
                            Log.d("DEBUG_EVENT","Event: ${event}")
                        }
                    }
                }
            }
        }
    }

}