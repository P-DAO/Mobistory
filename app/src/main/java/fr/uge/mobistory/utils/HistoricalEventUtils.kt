package fr.uge.mobistory.utils

import fr.uge.mobistory.database.HistoricalEventAndClaim
import fr.uge.mobistory.historicalEvent.claim.ClaimEntity
import java.util.*

/**
 * extraction du label en français sinon en anglais
 * TODO faire une option choix langue
 */
fun extractLabel(event: HistoricalEventAndClaim): String?{
    var frLabel = extractLabelFrFromLabel(event)
    var enLabel = extractLabelEnFromLabel(event)
    if(frLabel == null){
        return enLabel?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    }
    return frLabel?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
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
        return enLabel?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    }
    return frLabel?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
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