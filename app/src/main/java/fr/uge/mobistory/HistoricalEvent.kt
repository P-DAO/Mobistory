package fr.uge.mobistory

import androidx.room.*
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
@Entity(tableName = "historical_events")
data class HistoricalEvent(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "label") val label: String,
    @ColumnInfo(name ="aliases") val aliases: String?,
//    @ColumnInfo(name = "language") val language: String?,
    @ColumnInfo(name = "description") val description: String,
    // TODO parser pour obtenir la date d'un event par rapport a la description
//    @ColumnInfo(name = "date") val date: Date?,
    @ColumnInfo(name = "popularity") val popularity: Int,
//    @ColumnInfo(name = "location") val location: String?,
    @Embedded(prefix = "claims_") // indique a Room que les proprietes de la classe Claim doivent etre traitees comme des colonnes de la table historical_events avec un préfixe de colonne "claims_".
    @Ignore val claims: List<Claims>?
){
    // Constructeur vide
    constructor(
        id: Int,
        label: String,
        aliases: String?,
        description: String,
//        date: Date?,
        popularity: Int,
//        location: String?
    ) : this(id, label, aliases, description, /*date,*/ popularity, /*location,*/ null)
    data class Claims (
        @PrimaryKey(autoGenerate = true) val id: Long,
        @ColumnInfo(name = "eventId") val eventId: Int?,
        @ColumnInfo(name = "verboseName") val verboseName: String?,
        @ColumnInfo(name = "value") val value: String?,
        @ColumnInfo(name = "item") val item: String?,
    )
}