package fr.uge.mobistory.database

import androidx.room.Embedded
import androidx.room.Relation
import fr.uge.mobistory.historicalEvent.HistoricalEventEntity
import fr.uge.mobistory.historicalEvent.claim.ClaimEntity

data class HistoricalEventAndClaim(
    @Embedded val historicalEvent: HistoricalEventEntity,
    @Relation(
        parentColumn = "historicalEventId",
        entityColumn = "eventId",
        entity = ClaimEntity::class,
    )
    val claims: List<ClaimEntity>
)
