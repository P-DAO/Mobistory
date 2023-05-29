package fr.uge.mobistory.dao

import androidx.room.*
import fr.uge.mobistory.database.HistoricalEventAndClaim
import fr.uge.mobistory.historicalEvent.HistoricalEventEntity
import fr.uge.mobistory.historicalEvent.claim.ClaimEntity

@Dao
interface ClaimDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertClaim(claim: ClaimEntity)

}