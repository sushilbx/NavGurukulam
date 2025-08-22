package com.sushilbx.navgurukulam

import android.content.Context
import com.sushilbx.navgurukulam.room.AppDatabase
import com.sushilbx.navgurukulam.room.ScoreCard
import com.sushilbx.navgurukulam.room.Student
import com.sushilbx.navgurukulam.room.SyncStatus
import kotlinx.coroutines.delay

interface ApiService {
    suspend fun syncStudents(students: List<Student>): ApiResponse<List<Student>>
    suspend fun syncScoreCards(scoreCards: List<ScoreCard>): ApiResponse<List<ScoreCard>>

    companion object {
        fun create(context: Context): ApiService {
            return LocalApiService(context)
        }
    }
}

class LocalApiService(private val context: Context) : ApiService {

    private val db = AppDatabase.getDatabase(context)

    override suspend fun syncStudents(students: List<Student>): ApiResponse<List<Student>> {
        delay(500) // fake delay to simulate network

        students.forEach { incoming ->
            val existing = db.studentDao().getStudentById(incoming.id)
            if (existing == null || incoming.updatedAt > existing.updatedAt) {
                db.studentDao().insertOrUpdate(
                    incoming.copy(syncStatus = SyncStatus.SYNCED)
                )
            }
        }

        return ApiResponse(
            success = true,
            data = students.map { it.copy(syncStatus = SyncStatus.SYNCED) }
        )
    }

    override suspend fun syncScoreCards(scoreCards: List<ScoreCard>): ApiResponse<List<ScoreCard>> {
        delay(500)

        scoreCards.forEach { incoming ->
            val existing = db.scoreCardDao().getScoreCardById(incoming.id)
            if (existing == null || incoming.updatedAt > existing.updatedAt) {
                db.scoreCardDao().insertOrUpdate(
                    incoming.copy(syncStatus = SyncStatus.SYNCED)
                )
            }
        }

        return ApiResponse(
            success = true,
            data = scoreCards.map { it.copy(syncStatus = SyncStatus.SYNCED) }
        )
    }
}

data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: String? = null
)

