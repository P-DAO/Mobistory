package fr.uge.mobistory.database

import android.content.Context
import androidx.room.Room
import fr.uge.mobistory.dao.ClaimDao
import fr.uge.mobistory.dao.HistoricalEventsDao
import fr.uge.mobistory.historicalEvent.HistoricalEventEntity
import fr.uge.mobistory.historicalEvent.claim.ClaimEntity

class EventRepository(applicationContext: Context) {

    private val eventDatabase: EventDatabase = Room.databaseBuilder(
        applicationContext,
        EventDatabase::class.java,
        "event_database"
    ).build()

    private val historicalEventsDao: HistoricalEventsDao = eventDatabase.historicalEventDao()
    private val claimDao: ClaimDao = eventDatabase.claimDao()

    suspend fun insertEvents(events: List<HistoricalEventEntity>){
        historicalEventsDao.insertEvents(events)
    }

    fun insertClaim(claim: ClaimEntity){
        claimDao.insertClaim(claim)
    }

    fun getHistoricalEventWithClaims(): List<HistoricalEventAndClaim>{
        return historicalEventsDao.getHistoricalEventWithClaims()
    }
}