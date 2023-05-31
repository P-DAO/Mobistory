package fr.uge.mobistory.tri

import fr.uge.mobistory.database.HistoricalEventAndClaim
import fr.uge.mobistory.utils.extractDatesFromClaims
import fr.uge.mobistory.utils.extractGeo
import fr.uge.mobistory.utils.extractGeoLatitudeLongiture
import fr.uge.mobistory.utils.extractLabel

fun sortByDate(events: List<HistoricalEventAndClaim>): List<HistoricalEventAndClaim>{
    return events.filter { extractDatesFromClaims(it.claims) != null }
        .sortedBy { extractDatesFromClaims(it.claims).first }
}

fun sortByLocation(events: List<HistoricalEventAndClaim>): List<HistoricalEventAndClaim>{
    return events.filter { extractGeoLatitudeLongiture(extractGeo(it.claims)) != null }
        .sortedBy { extractGeoLatitudeLongiture(extractGeo(it.claims)).first }
}

fun sortByLabel(events: List<HistoricalEventAndClaim>): List<HistoricalEventAndClaim>{
    return events.filter{ extractLabel(it) != null }
        .sortedBy { extractLabel(it) }
}

// trie en fr pas en en
// TODO modifier ici pour langue en
fun sortByPopularity(events: List<HistoricalEventAndClaim>): List<HistoricalEventAndClaim>{
    return events.filter { it.historicalEvent.popularity.fr != null }
        .sortedBy { it.historicalEvent.popularity.fr }
}