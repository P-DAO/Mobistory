package fr.uge.mobistory.database

import androidx.room.*
import fr.uge.mobistory.Claim

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
    @ColumnInfo(name = "popularity") val popularity: Int,
//    @ColumnInfo(name = "location") val location: String?,
    @ColumnInfo(name = "claims") // indique a Room que les proprietes de la classe Claim doivent etre traitees comme des colonnes de la table historical_events avec un préfixe de colonne "claims_".
    val claims: List<Claim>?
)



