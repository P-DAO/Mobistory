package fr.uge.mobistory

import fr.uge.mobistory.database.ClaimEntity
import kotlinx.serialization.Serializable

@Serializable
data class Claim (
    val idClaim: Int,
    val verboseName: String?,
    val value: String?,
    val item: String?,
    val formatterUrl: String?
){
    fun toClaimEntity(idEvent: Int): ClaimEntity {
        return ClaimEntity(null, idClaim, idEvent, verboseName, value, item, formatterUrl)
    }
}
