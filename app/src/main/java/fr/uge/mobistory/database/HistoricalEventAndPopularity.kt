package fr.uge.mobistory.database

import androidx.room.Embedded
import androidx.room.Relation
import fr.uge.mobistory.historicalEvent.HistoricalEventEntity
import fr.uge.mobistory.historicalEvent.claim.ClaimEntity
import fr.uge.mobistory.historicalEvent.popularity.PopularityEntity

data class HistoricalEventAndPopularity(
    @Embedded val historicalEvent: HistoricalEventEntity,
    @Relation(
        parentColumn = "historicalEventId",
        entityColumn = "eventId",
        entity = PopularityEntity::class,
    )
    val popularyty: List<PopularityEntity>
)
