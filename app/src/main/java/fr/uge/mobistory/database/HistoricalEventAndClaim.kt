package fr.uge.mobistory.database

import androidx.room.Embedded
import androidx.room.Relation
import fr.uge.mobistory.Claim
import fr.uge.mobistory.HistoricalEvent

data class HistoricalEventAndClaim(
    @Embedded val historicalEvent: HistoricalEvent,
    @Relation(
        parentColumn = "historicalEventId",
        entityColumn = "eventId"
    )
    val claim: List<Claim>
)
