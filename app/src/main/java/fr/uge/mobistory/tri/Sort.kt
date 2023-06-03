package fr.uge.mobistory.tri

import android.os.Build
import androidx.annotation.RequiresApi
import fr.uge.mobistory.database.HistoricalEventAndClaim
import fr.uge.mobistory.historicalEvent.claim.ClaimEntity
import fr.uge.mobistory.utils.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Tri par date
 */
@RequiresApi(Build.VERSION_CODES.O)
fun sortByDate(events: List<HistoricalEventAndClaim>): List<HistoricalEventAndClaim> {
    return events.sortedWith(Comparator { event1, event2 ->
        val date1 = extractDatesFromClaims(event1.claims)
        val date2 = extractDatesFromClaims(event2.claims)
        if (date1 != null && date2 != null) {
            return@Comparator date2.compareTo(date1)
        } else if (date1 != null) {
            return@Comparator -1
        } else if (date2 != null) {
            return@Comparator 1
        }
        return@Comparator 0
    })
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