package fr.uge.mobistory.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.uge.mobistory.database.EventRepository
import fr.uge.mobistory.historicalEvent.HistoricalEvent
import fr.uge.mobistory.historicalEvent.HistoricalEventEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.streams.toList

class ImportEventTxt : ViewModel(){
    @RequiresApi(Build.VERSION_CODES.N)
    fun importEventTxtInDataBase(fileContent: List<String>, eventRepository: EventRepository){
        viewModelScope.launch(Dispatchers.IO) {
            val listHistoricalEvent: List<HistoricalEvent> = fileContent.asSequence()
                .map { line ->
                    Json { ignoreUnknownKeys = true }.decodeFromString<HistoricalEvent>(
                        line
                    )
                }
//                            .peek { event -> Log.d("event", event.toString()) }
                .toList()

            // Désérialisation des données JSON en objets HistoricalEvent
            val historicalEventEntities: List<HistoricalEventEntity> = listHistoricalEvent.asSequence()
                .map { event -> event.toHistoricalEventEntity() }
//                            .peek { event -> Log.d("Event_ENTITY", event.toString()) }
                .toList()

            listHistoricalEvent.forEach { event ->
                event.claims.forEach {
                    val claimEntity = it.toClaimEntity(event.id)
//                            Log.d("ClaimEntity", claimEntity.toString())
                    eventRepository.insertClaim(claimEntity)
                }
            }

            // Importer les événements dans la base de données
            eventRepository.insertEvents(historicalEventEntities)

            // DEBUG
            // Récupérer tous les événements depuis la base de données
//            val allEvents = eventRepository.getHistoricalEventWithClaims()
            // Afficher les événements dans la console
//            for (event in allEvents) {
//                Log.d("DEBUG_EVENT","Event: ${event}")
//            }
        }

    }
}