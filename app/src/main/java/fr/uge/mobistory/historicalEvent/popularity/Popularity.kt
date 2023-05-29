package fr.uge.mobistory.historicalEvent.popularity

import kotlinx.serialization.Serializable

@Serializable
data class Popularity (
    val en: Int = -1,
    val fr: Int = -1
){
    fun toPopularityEntity(idEvent: Int): PopularityEntity{
        return PopularityEntity(0, idEvent, fr)
    }
}
