package fr.uge.mobistory.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ClaimEntity (
        @PrimaryKey(autoGenerate = true) val id: Long,
        @ColumnInfo(name = "idClaim") val idClaim: Int,
        @ColumnInfo(name = "eventId") val eventId: Int,
        @ColumnInfo(name = "verboseName") val verboseName: String?,
        @ColumnInfo(name = "value") val value: String?,
        @ColumnInfo(name = "item") val item: String?,
        @ColumnInfo(name = "formatterUrl") val formatterUrl: String?,
)
