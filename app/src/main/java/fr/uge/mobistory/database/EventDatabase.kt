package fr.uge.mobistory.database

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.uge.mobistory.HistoricalEvent
import fr.uge.mobistory.dao.HistoricalEventsDao

@Database(entities = [HistoricalEventEntity::class], version = 1)
//@TypeConverters(DateConverter::class)
abstract class EventDatabase : RoomDatabase (){
    abstract fun historicalEventDao(): HistoricalEventsDao
}