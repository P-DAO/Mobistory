package fr.uge.mobistory.historicalEvent.claim

import fr.uge.mobistory.historicalEvent.HistoricalEvent
import fr.uge.mobistory.historicalEvent.claim.ClaimEntity
import kotlinx.serialization.Serializable

@Serializable
data class Claim (
    val id: Int,
    val verboseName: String = "",
    val value: String = "",
    val item: HistoricalEvent? = null,
    val formatterUrl: String = ""
){
    fun toClaimEntity(idEvent: Int): ClaimEntity {
        return ClaimEntity( 0, id, idEvent, verboseName, value, formatterUrl)
    }
}
