package fr.uge.mobistory.tri

import android.os.Build
import androidx.annotation.RequiresApi
import fr.uge.mobistory.database.HistoricalEventAndClaim
import fr.uge.mobistory.historicalEvent.claim.ClaimEntity
import fr.uge.mobistory.utils.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun sortByDate(events: List<HistoricalEventAndClaim>): List<HistoricalEventAndClaim> {
    return events.filter { extractDatesFromClaims(it.claims) != null }
//        .sortedBy { extractDatesFromClaims(it.claims) }
        .sortedWith(compareBy { extractYearFromDate(it) })
}

@RequiresApi(Build.VERSION_CODES.O)
fun extractYearFromDate(event: HistoricalEventAndClaim): Int? {
    val date = extractDatesFromClaims(event.claims)
    return date?.let {
        val yearComponent = it.split("/").lastOrNull()
        yearComponent?.toIntOrNull()
    }
}

fun sortByLocation(events: List<HistoricalEventAndClaim>): List<HistoricalEventAndClaim> {
    return events.filter { extractGeoLatitudeLongiture(extractGeo(it.claims)) != null }
        .sortedBy { extractGeoLatitudeLongiture(extractGeo(it.claims)).first }
}

fun sortByLabel(events: List<HistoricalEventAndClaim>): List<HistoricalEventAndClaim> {
    return events.filter { extractLabel(it) != null }
        .sortedBy { extractLabel(it) }
}

// trie en fr pas en en
// TODO modifier ici pour langue en
fun sortByPopularity(events: List<HistoricalEventAndClaim>): List<HistoricalEventAndClaim> {
    return events.filter { it.historicalEvent.popularity.fr != null && it.historicalEvent.popularity.fr != -1 }
        .sortedBy { it.historicalEvent.popularity.fr }
}