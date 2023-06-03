package fr.uge.mobistory.utils

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import fr.uge.mobistory.historicalEvent.claim.ClaimEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun extractDatesFromClaims(claims: List<ClaimEntity>): String? {
    val datePeriod = extractDatesPeriodFromClaims(claims);
    return if (datePeriod.first != null && datePeriod.second == null) {
        " ${datePeriod.first}"
    } else if (datePeriod.first != null && datePeriod.second != null) {
        " de ${datePeriod.first} à ${datePeriod.second}"
    } else {
        extractDatesPunctualFromClaims(claims)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun extractDatesPeriodFromClaims(claims: List<ClaimEntity>): Pair<String?, String?> {
    var startDate: String? = null
    var endDate: String? = null

    for (claim in claims) {
        val verboseName = claim.verboseName
        val value = claim.value

        if (verboseName == "fr:date de début||en:start time") {
            startDate = extractDateFromValue(value.orEmpty())
        } else if (verboseName == "fr:date de fin||en:end time") {
            endDate = extractDateFromValue(value.orEmpty())
        }
    }
    return Pair(startDate, endDate)
}

@RequiresApi(Build.VERSION_CODES.O)
fun extractDatesPunctualFromClaims(claims: List<ClaimEntity>): String? {
    var date: String? = null

    for (claim in claims) {
        val verboseName = claim.verboseName
        val value = claim.value

        if (verboseName == "fr:date||en:point in time") {
            date = extractDateFromValue(value.orEmpty())
        }
    }
    return date
}

@RequiresApi(Build.VERSION_CODES.O)
fun extractDateFromValue(value: String): String? {
    val datePrefix = "date:"
    var date: String? = null;
    if (value.startsWith(datePrefix)) {
        val dateValue = value.substringAfter(datePrefix)
        val component = dateValue.split("-")

        if (component.size == 4) {
            val year = component[1]
            return "-${year} av. J-C"
        }
        if (component.size == 3) {
            val year = component[0]
            val month = component[1]
            val day = component[2]

            if (month != "0" && day != "0") {
                val date = LocalDate.of(year.toInt(), month.toInt(), day.toInt())
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                return date.format(formatter)
            }

            date = component[0]
        } else if(component.size == 1){
            return component[0]
        }
    }

    return date
}

/**
 * extraction des données gps
 */
fun extractGeoLatitudeLongiture(coordinate: String?): Pair<String?, String?> {
    var parts: List<String> = listOf()
    var latitude: String? = null
    var longitude: String? = null

    if (coordinate != null) {
        parts = coordinate.split(",")
        latitude = parts.getOrNull(0)
        longitude = parts.getOrNull(1)
    }

    return Pair(latitude, longitude)
}

fun extractGeo(claims: List<ClaimEntity>): String? {
    for (claim in claims) {
        val verboseName = claim.verboseName
        val value = claim.value

        if (verboseName == "fr:coordonnées géographiques||en:coordinate location") {
            return extractGeoFromValue(value.orEmpty())
        }
    }
    return null

}

fun extractGeoFromValue(value: String): String? {
    val geoPrefix = "geo:"
    if (value.startsWith(geoPrefix)) {
        return value.substringAfter(geoPrefix)
    }
    return null
}