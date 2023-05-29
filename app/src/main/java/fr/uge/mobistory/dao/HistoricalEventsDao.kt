package fr.uge.mobistory.dao

import androidx.room.*
import fr.uge.mobistory.database.HistoricalEventAndClaim
import fr.uge.mobistory.historicalEvent.HistoricalEventEntity

@Dao
interface HistoricalEventsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<HistoricalEventEntity>)

    @Query("SELECT * FROM historical_event")
    fun getAll(): List<HistoricalEventEntity>

    @Query("SELECT * FROM historical_event WHERE label LIKE :label AND " +
            "description LIKE :description LIMIT 1")
    fun findByName(label: String, description: String): HistoricalEventEntity

    // TODO decomm si date a bien ete recuperee
//    @Query("SELECT * FROM historical_event ORDER BY date ASC")
//    fun getEventsOrderByDate() : Flow<List<HistoricalEvent>>

    @Transaction
    @Query("SELECT * FROM historical_event")
    fun getHistoricalEventWithClaims(): List<HistoricalEventAndClaim>

}