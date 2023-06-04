package fr.uge.mobistory.tri

import fr.uge.mobistory.database.HistoricalEventAndClaim
import fr.uge.mobistory.historicalEvent.claim.ClaimEntity
import fr.uge.mobistory.utils.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Tri par date
 */
fun sortByDate(events: List<HistoricalEventAndClaim>): List<HistoricalEventAndClaim> {
    return events.sortedBy { event ->
        val date = extractDateFromClaimsForSort(event.claims)
        date?.time ?: 0
    }
}

fun sortByDateDescending(events: List<HistoricalEventAndClaim>): List<HistoricalEventAndClaim> {
    return events.sortedByDescending { event ->
        val date = extractDateFromClaimsForSort(event.claims)
        date?.time ?: 0
    }
}
fun extractDateFromClaimsForSort(claims: List<ClaimEntity>): Date? {

    for (claim in claims) {
        val verboseName = claim.verboseName
        val value = claim.value

        if (verboseName == "fr:date de début||en:start time") {
            return extractValue(value.orEmpty())
        } else if (verboseName == "fr:date||en:point in time") {
            return extractValue(value.orEmpty())
        }
    }
    return null
}

fun extractValue(value: String): Date? {
    val datePrefix = "date:"
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    val date =  value.substringAfter(datePrefix)
    return try {
        formatter.parse(date)
    } catch (e: Exception) {
        null
    }

}

/**
 * Tri par localisation
 */
fun sortByLocation(events: List<HistoricalEventAndClaim>): List<HistoricalEventAndClaim> {
    return events.filter { extractGeoLatitudeLongiture(extractGeo(it.claims)) != null }
        .sortedBy { extractGeoLatitudeLongiture(extractGeo(it.claims)).first }
}

/**
 * Tri par label d'évènement
 */
fun sortByLabel(events: List<HistoricalEventAndClaim>): List<HistoricalEventAndClaim> {
    return events.filter { extractLabel(it) != null }
        .sortedBy { extractLabel(it) }
}

/**
 * Tri par popularité fr
 */
// TODO modifier ici pour langue en
fun sortByPopularity(events: List<HistoricalEventAndClaim>): List<HistoricalEventAndClaim> {
    return events.filter { it.historicalEvent.popularity.fr != null && it.historicalEvent.popularity.fr != -1 }
        .sortedBy { it.historicalEvent.popularity.fr }
}