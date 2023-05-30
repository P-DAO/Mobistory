package fr.uge.mobistory.utils

import fr.uge.mobistory.database.HistoricalEventAndClaim

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


fun extractDescription(event: HistoricalEventAndClaim): String?{
    var frLabel = extractDescriptionFrFromDescription(event)
    var enLabel = extractDescriptionFromDescription(event)
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

fun extractDescriptionFromDescription(event: HistoricalEventAndClaim): String?{
    val enPrefix = "en:"
    if(event.historicalEvent.description.startsWith(enPrefix)){
        return  event.historicalEvent.description.substringAfter(enPrefix)
    }
    return null
}