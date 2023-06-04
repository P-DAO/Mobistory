package fr.uge.mobistory.historicalEvent

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.room.*
import fr.uge.mobistory.historicalEvent.popularity.Popularity

@Entity(tableName = "historical_event")
data class HistoricalEventEntity(
    @PrimaryKey
    val historicalEventId: Int,
    val label: String,
    val aliases: String?,
//    val language: String?,
    val description: String,
    @Embedded
    val popularity: Popularity
    // TODO parser pour obtenir la date d'un event par rapport a la description
//    @ColumnInfo(name = "date") val date: Date?,
//    @ColumnInfo(name = "wikipedia") val wikipedia: String?,
//    @ColumnInfo(name = "location") val location: String?,

)



