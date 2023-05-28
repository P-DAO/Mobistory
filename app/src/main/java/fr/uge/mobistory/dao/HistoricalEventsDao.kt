package fr.uge.mobistory.dao

import androidx.room.*
import fr.uge.mobistory.Claim
import fr.uge.mobistory.HistoricalEvent

@Dao
interface HistoricalEventsDao {

    @Upsert // remplace si la ligne existe deja (maj)
    suspend fun upsertEvents(events: List<HistoricalEvent>)

    @Query("SELECT * FROM historical_event")
    fun getAll(): List<HistoricalEvent>

    @Query("SELECT * FROM historical_event WHERE label LIKE :label AND " +
            "description LIKE :description LIMIT 1")
    fun findByName(label: String, description: String): HistoricalEvent

    // TODO decomm si date a bien ete recuperee
//    @Query("SELECT * FROM historical_event ORDER BY date ASC")
//    fun getEventsOrderByDate() : Flow<List<HistoricalEvent>>

    @Transaction
    @Query("SELECT * FROM historical_event")
    fun getHistoricalEventWithClaims(): List<Claim>

    @Delete
    suspend fun delete(event: HistoricalEvent)

}