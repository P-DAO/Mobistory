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