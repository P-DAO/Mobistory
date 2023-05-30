package fr.uge.mobistory.utils

import fr.uge.mobistory.historicalEvent.claim.ClaimEntity

fun extractDatesFromClaims(claims: List<ClaimEntity>) : Pair<String?, String?>{
    var startDate: String? = null
    var endDate: String? = null

    for (claim in claims) {
        val verboseName = claim.verboseName
        val value = claim.value

        if(verboseName == "fr:date de début||en:start time"){
            startDate = extractDateFromValue(value.orEmpty())
        }else if(verboseName == "fr:date de fin||en:end time"){
            endDate = extractDateFromValue(value.orEmpty())
        }
    }
    return Pair(startDate, endDate)
}

fun extractDateFromValue(value: String): String? {
    val datePrefix = "date:"
    if(value.startsWith(datePrefix)){
        return  value.substringAfter(datePrefix)
    }
    return null
}