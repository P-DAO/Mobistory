package fr.uge.mobistory.utils

import fr.uge.mobistory.database.HistoricalEventAndClaim
import fr.uge.mobistory.historicalEvent.claim.ClaimEntity

/**
 * extraction du label en français sinon en anglais
 * TODO faire une option choix langue
 */
fun extractLabel(event: HistoricalEventAndClaim): String?{
    var frLabel = extractLabelFrFromLabel(event)
    var enLabel = extractLabelEnFromLabel(event)
    if(frLabel == null){
        return enLabel
    }
    return frLabel
}

fun extractLabelFrFromLabel(event: HistoricalEventAndClaim): String?{
    var regex = Regex("fr:(.*?)(?:\\|\\|en:|$)")
    var matchResult = regex.find(event.historicalEvent.label)
    return matchResult?.groupValues?.getOrNull(1)
}

fun extractLabelEnFromLabel(event: HistoricalEventAndClaim): String?{
    val enPrefix = "en:"
    if(event.historicalEvent.label.startsWith(enPrefix)){
        return  event.historicalEvent.label.substringAfter(enPrefix)
    }
    return null
}

/**
 * extraction de la descriotion en français sinon en anglais
 * TODO faire option choix langue
 */
fun extractDescription(event: HistoricalEventAndClaim): String?{
    var frLabel = extractDescriptionFrFromDescription(event)
    var enLabel = extractDescriptionEnFromDescription(event)
    if(frLabel == null){
        return enLabel
    }
    return frLabel
}

fun extractDescriptionFrFromDescription(event: HistoricalEventAndClaim): String? {
    var regex = Regex("fr:(.*?)(?:\\|\\|en:|$)")
    var matchResult = regex.find(event.historicalEvent.description)
    return matchResult?.groupValues?.getOrNull(1)
}

fun extractDescriptionEnFromDescription(event: HistoricalEventAndClaim): String?{
    val enPrefix = "en:"
    if(event.historicalEvent.description.startsWith(enPrefix)){
        return  event.historicalEvent.description.substringAfter(enPrefix)
    }
    return null
}

/**
 * extraction des données gps
 */
fun extractGeoLatitudeLongiture(coordinate: String?): Pair<String?, String?>{
    var parts: List<String> = listOf()
    var latitude: String? = null
    var longitude: String? = null

    if(coordinate != null){
        parts = coordinate.split(",")
        latitude = parts.getOrNull(0)
        longitude = parts.getOrNull(1)
    }

    return Pair(latitude, longitude)
}
fun extractGeo(claims: List<ClaimEntity>): String?{
    for (claim in claims) {
        val verboseName = claim.verboseName
        val value = claim.value

        if(verboseName == "fr:coordonnées géographiques||en:coordinate location"){
            return extractGeoFromValue(value.orEmpty())
        }
    }
    return null

}
fun extractGeoFromValue(value: String): String? {
    val geoPrefix = "geo:"
    if(value.startsWith(geoPrefix)){
        return  value.substringAfter(geoPrefix)
    }
    return null
}