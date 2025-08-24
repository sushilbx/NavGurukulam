package com.sushilbx.navgurukulam.room
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
@Dao
interface ScoreCardDao {
    @Query("SELECT * FROM scorecards WHERE studentId = :studentId AND deleted = 0 ORDER BY subject ASC")
    fun observeForStudent(studentId: String): Flow<List<ScoreCard>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: ScoreCard)

    @Query("UPDATE scorecards SET deleted = 1, updatedAt = :ts, syncStatus = :status WHERE id = :id")
    suspend fun softDelete(id: String, ts: Instant, status: SyncStatus)

    @Query("SELECT * FROM scorecards WHERE syncStatus IN ('PENDING','FAILED') ORDER BY updatedAt ASC")
    suspend fun pendingOrFailed(): List<ScoreCard>

    @Query("UPDATE scorecards SET syncStatus = :status, remoteId = :remoteId, updatedAt = :updatedAt WHERE id = :id")
    suspend fun markSynced(id: String, status: SyncStatus, remoteId: String?, updatedAt: Instant)

    @Query("DELETE FROM scorecards WHERE id = :id")
    suspend fun hardDelete(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(scoreCard: ScoreCard): Long

    @Update
    suspend fun update(scoreCard: ScoreCard)

    @Delete
    suspend fun delete(scoreCard: ScoreCard)


    @Query("SELECT * FROM scorecards WHERE studentId = :studentId")
    suspend fun getByStudent(studentId: String): List<ScoreCard>

    @Query("SELECT * FROM scorecards WHERE syncStatus != 'SYNCED'")
    suspend fun getPending(): List<ScoreCard>
}
