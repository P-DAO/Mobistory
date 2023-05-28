package fr.uge.mobistory.historicalEvent

import androidx.room.*

@Entity(tableName = "historical_event")
data class HistoricalEventEntity(
    @PrimaryKey
    val historicalEventId: Int,
    @ColumnInfo(name = "label") val label: String,
    @ColumnInfo(name ="aliases") val aliases: String?,
//    @ColumnInfo(name = "language") val language: String?,
    @ColumnInfo(name = "description") val description: String,
    // TODO parser pour obtenir la date d'un event par rapport a la description
//    @ColumnInfo(name = "date") val date: Date?,
//    @ColumnInfo(name = "wikipedia") val wikipedia: String?,
//    @ColumnInfo(name = "location") val location: String?,

)



