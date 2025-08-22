package com.sushilbx.navgurukulam

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sushilbx.navgurukulam.room.AppDatabase
/*
class SyncWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    private val db = AppDatabase.getInstance(appContext)
    private val api = ApiService.create()

    override suspend fun doWork(): Result {
        val studentRepo = StudentRepository(db.studentDao(), api)
        val scoreRepo = ScoreCardRepository(db.scoreCardDao(), api)

        // Sync Students first
        studentRepo.syncPendingStudents()
        // Then ScoreCards
        scoreRepo.syncPendingScoreCards()

        return Result.success()
    }
}*/
