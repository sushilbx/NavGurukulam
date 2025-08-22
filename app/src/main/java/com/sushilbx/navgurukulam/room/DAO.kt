package com.sushilbx.navgurukulam.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(student: Student)

    @Update
    suspend fun update(student: Student)

    @Delete
    suspend fun delete(student: Student)

    @Query("SELECT * FROM students ORDER BY updatedAt DESC")
    fun getAllStudents(): Flow<List<Student>>

    @Query("SELECT * FROM students WHERE syncStatus != 'SYNCED'")
    suspend fun getPendingStudents(): List<Student>

    @Query("SELECT * FROM students WHERE id = :id LIMIT 1")
    suspend fun getStudentById(id: Int): Student?

    suspend fun insertOrUpdate(student: Student) {
        val existing = getStudentById(student.id)
        if (existing == null) {
            insert(student)
        } else {
            update(student)
        }
    }
}


@Dao
interface ScoreCardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(scoreCard: ScoreCard)

    @Update
    suspend fun update(scoreCard: ScoreCard)

    @Delete
    suspend fun delete(scoreCard: ScoreCard)

    @Query("SELECT * FROM scorecards WHERE studentId = :studentId")
    fun getScoreCardsForStudent(studentId: String): Flow<List<ScoreCard>>

    @Query("SELECT * FROM scorecards WHERE syncStatus != 'SYNCED'")
    suspend fun getPendingScoreCards(): List<ScoreCard>

    @Query("SELECT * FROM scorecards WHERE id = :id LIMIT 1")
    suspend fun getScoreCardById(id: String): ScoreCard?

    suspend fun insertOrUpdate(scoreCard: ScoreCard) {
        val existing = getScoreCardById(scoreCard.id)
        if (existing == null) {
            insert(scoreCard)
        } else {
            update(scoreCard)
        }
    }
}
