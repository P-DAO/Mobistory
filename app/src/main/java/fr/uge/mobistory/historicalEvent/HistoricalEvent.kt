package fr.uge.mobistory.historicalEvent

import android.os.Build
import androidx.annotation.RequiresApi
import fr.uge.mobistory.historicalEvent.claim.Claim
import fr.uge.mobistory.historicalEvent.popularity.Popularity
import fr.uge.mobistory.historicalEvent.claim.ClaimEntity
import kotlinx.serialization.Serializable
import kotlin.streams.toList

@Serializable
data class HistoricalEvent(

    val id: Int,
    val label: String = "",
    val aliases: String = "",
    val description: String = "",
    val wikipedia: String = "",
    val popularity: Popularity = Popularity(-1, -1 ),
    val sourceId: Int = -1,
    val claims : List<Claim> = listOf()
){
    fun toHistoricalEventEntity(): HistoricalEventEntity {
        return HistoricalEventEntity(id, label, aliases, description, popularity)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun toListClaimEntity(): List<ClaimEntity> {
        return claims.stream().map { claim -> claim.toClaimEntity(id) }.toList()
    }

}
