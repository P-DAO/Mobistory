package fr.uge.mobistory

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HistoricalEvent::class], version = 1)
//@TypeConverters(DateConverter::class)
abstract class EventDatabase : RoomDatabase (){
    abstract fun historicalEventsDao(): HistoricalEventsDao
}