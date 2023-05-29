package fr.uge.mobistory.database

import android.content.Context
import androidx.room.Room
import fr.uge.mobistory.dao.ClaimDao
import fr.uge.mobistory.dao.HistoricalEventsDao
import fr.uge.mobistory.historicalEvent.HistoricalEventEntity
import fr.uge.mobistory.historicalEvent.claim.ClaimEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EventRepository(applicationContext: Context) {

    private val eventDatabase: EventDatabase = Room.databaseBuilder(
        applicationContext,
        EventDatabase::class.java,
        "event_database"
    ).build()

    private val historicalEventsDao: HistoricalEventsDao = eventDatabase.historicalEventDao()
    private val claimDao: ClaimDao = eventDatabase.claimDao()

    suspend fun insertEvents(events: List<HistoricalEventEntity>){
        withContext(Dispatchers.IO){
            historicalEventsDao.insertEvents(events)
        }
    }

    fun insertClaim(claim: ClaimEntity){
        claimDao.insertClaim(claim)
    }

    suspend fun getHistoricalEventWithClaims(): List<HistoricalEventAndClaim>{
        return withContext(Dispatchers.IO){
            historicalEventsDao.getHistoricalEventWithClaims()
        }
    }

    fun getAll(): List<HistoricalEventEntity>{
        return historicalEventsDao.getAll()
    }
}