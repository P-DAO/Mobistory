package fr.uge.mobistory.historicalEvent.popularity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PopularityEntity (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "eventId") val eventId: Int,
    @ColumnInfo("fr") val fr: Int,
)
