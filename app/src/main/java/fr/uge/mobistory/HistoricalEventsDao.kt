package fr.uge.mobistory

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.concurrent.Flow

@Dao
interface HistoricalEventsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) // remplace si la ligne existe deja (maj)
    suspend fun insertEvents(events: List<HistoricalEvent>)

    @Query("SELECT * FROM historical_events")
    fun getAll(): List<HistoricalEvent>

    @Query("SELECT * FROM historical_events WHERE label LIKE :label AND " +
            "description LIKE :description LIMIT 1")
    fun findByName(label: String, description: String): HistoricalEvent

    // TODO decomm si date a bien ete recuperee
//    @Query("SELECT * FROM historical_events ORDER BY date ASC")
//    fun getEventsOrderByDate() : Flow<List<HistoricalEvent>>

    @Delete
    fun delete(event: HistoricalEvent)

}