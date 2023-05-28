package fr.uge.mobistory.database

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.uge.mobistory.dao.HistoricalEventsDao
import fr.uge.mobistory.historicalEvent.HistoricalEventEntity
import fr.uge.mobistory.historicalEvent.claim.ClaimEntity
import fr.uge.mobistory.historicalEvent.popularity.PopularityEntity

@Database(entities = [HistoricalEventEntity::class, ClaimEntity::class, PopularityEntity::class], version = 1)
//@TypeConverters(DateConverter::class)
abstract class EventDatabase : RoomDatabase (){
    abstract fun historicalEventDao(): HistoricalEventsDao
}