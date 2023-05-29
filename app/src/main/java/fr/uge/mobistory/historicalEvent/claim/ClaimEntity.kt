package fr.uge.mobistory.historicalEvent.claim

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ClaimEntity (
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        val idClaim: Int,
        val eventId: Int,
        val verboseName: String?,
        val value: String?,
//        val item: HistoricalEventEntity?,
        val formatterUrl: String?,
)
