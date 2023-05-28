package fr.uge.mobistory.historicalEvent

import android.os.Build
import androidx.annotation.RequiresApi
import fr.uge.mobistory.historicalEvent.claim.Claim
import fr.uge.mobistory.historicalEvent.popularity.Popularity
import fr.uge.mobistory.historicalEvent.claim.ClaimEntity
import fr.uge.mobistory.historicalEvent.popularity.PopularityEntity
import kotlinx.serialization.Serializable
import kotlin.streams.toList

@Serializable
data class HistoricalEvent(

    val id: Int,
    val label: String,
    val aliases: String?,
    val description: String,
    val wikipedia: String?,
    val popularity: Popularity,
    val sourceId: Int?,
    val claims : List<Claim>
){
    fun toHistoricalEventEntity(): HistoricalEventEntity {
        return HistoricalEventEntity(id, label, aliases, description)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun toListClaimEntity(): List<ClaimEntity> {
        return claims.stream().map { claim -> claim.toClaimEntity(id) }.toList()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun toListPopularity(): PopularityEntity {
        return PopularityEntity(0, id , popularity.fr)
    }

}
