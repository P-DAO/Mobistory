package fr.uge.mobistory.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import fr.uge.mobistory.dao.ClaimDao
import fr.uge.mobistory.dao.HistoricalEventsDao
import fr.uge.mobistory.historicalEvent.HistoricalEventEntity
import fr.uge.mobistory.historicalEvent.claim.ClaimEntity

@Database(entities = [HistoricalEventEntity::class, ClaimEntity::class], version = 1)
abstract class EventDatabase : RoomDatabase (){
    abstract fun historicalEventDao(): HistoricalEventsDao
    abstract fun claimDao(): ClaimDao

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: EventDatabase? = null

        fun getInstance(context: Context): EventDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): EventDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                EventDatabase::class.java, "event_database"
            ).build()
        }
    }
}