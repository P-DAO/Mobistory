package fr.uge.mobistory

import fr.uge.mobistory.database.HistoricalEventEntity
import kotlinx.serialization.Serializable

@Serializable
data class HistoricalEvent(

    val historicalEventId: Int,
    val label: String,
    val aliases: String?,
//   val language: String?,
    val description: String,
    // TODO parser pour obtenir la date d'un event par rapport a la description
//    val date: Date?,
    val popularity: Int,
//    val location: String?,
    val claims: List<Claim>?
){
    fun toHistoricalEventEntity(): HistoricalEventEntity {
        return HistoricalEventEntity(historicalEventId, label, aliases, description, popularity, claims)
    }
}
