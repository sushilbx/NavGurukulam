package com.sushilbx.navgurukulam.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
@Dao
interface StudentDao {
    @Query("SELECT * FROM students WHERE deleted = 0 ORDER BY fullName ASC")
    fun observeAll(): Flow<List<StudentWithScores>>

    @Query("SELECT * FROM students WHERE id = :id")
    suspend fun getById(id: String): Student?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(student: Student)

    @Update
    suspend fun update(student: Student)

    @Query("UPDATE students SET deleted = 1, updatedAt = :ts, syncStatus = :status WHERE id = :id")
    suspend fun softDelete(id: String, ts: Instant, status: SyncStatus)

    @Query("SELECT * FROM students WHERE syncStatus IN ('PENDING','FAILED') ORDER BY updatedAt ASC")
    suspend fun pendingOrFailed(): List<Student>

    @Query("UPDATE students SET syncStatus = :status, remoteId = :remoteId, updatedAt = :updatedAt WHERE id = :id")
    suspend fun markSynced(id: String, status: SyncStatus, remoteId: String?, updatedAt: Instant)

    @Query("DELETE FROM students WHERE id = :id")
    suspend fun hardDelete(id: String)

    // 🔹 Fetch students with their scores
    @Transaction
    @Query("SELECT * FROM students WHERE deleted = 0 ORDER BY fullName ASC")
    fun observeAllWithScores(): Flow<List<StudentWithScores>>

    @Transaction
    @Query("SELECT * FROM students WHERE id = :id")
    suspend fun getByIdWithScores(id: String): StudentWithScores?


}
