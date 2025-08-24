package com.sushilbx.navgurukulam

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sushilbx.navgurukulam.apis.ApiService
import com.sushilbx.navgurukulam.apis.toDto
import com.sushilbx.navgurukulam.apis.toEntity
import com.sushilbx.navgurukulam.room.AppDatabase
import com.sushilbx.navgurukulam.room.SyncStatus
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class SyncWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    private val db = AppDatabase.get(appContext)
    private val api = ApiService.create()
    private val prefs = Prefs(appContext)

    override suspend fun doWork(): Result = try {
        pushStudents()
        pushScoreCards()


        pullStudents()
        pullScoreCards()

        Result.success()
    } catch (e: Exception) {
        Result.retry()
    }

    private suspend fun pushStudents() {
        val dao = db.studentDao()
        dao.pendingOrFailed().forEach { local ->
            try {
                val res = api.upsertStudent(local.toDto())
                dao.markSynced(
                    id = local.id,
                    status = SyncStatus.SYNCED,
                    remoteId = local.remoteId ?: res.id,
                    updatedAt = Instant.parse(res.updatedAt)
                )
                if (local.deleted) dao.hardDelete(local.id) // remote deletion overrides -> remove locally
            } catch (e: Exception) {
                dao.markSynced(local.id, SyncStatus.FAILED, local.remoteId, local.updatedAt)
            }
        }
    }

    private suspend fun pushScoreCards() {
        val studentMap = db.studentDao().pendingOrFailed()
        val students = db.studentDao()
        val idToRemote = HashMap<String, String?>().apply {
            db.studentDao().pendingOrFailed()
        }
        db.studentDao().observeAll()
        db.query("SELECT id, remoteId FROM students", null).use {  }

        val dao = db.scoreCardDao()
        dao.pendingOrFailed().forEach { local ->
            try {
                val remoteStudentId = db.studentDao().getById(local.studentId.toString())?.remoteId
                    ?: return@forEach
                val res = api.upsertScoreCard(local.toDto(remoteStudentId))
                dao.markSynced(
                    id = local.id,
                    status = SyncStatus.SYNCED,
                    remoteId = local.remoteId ?: res.id,
                    updatedAt = Instant.parse(res.updatedAt)
                )
                if (local.deleted) dao.hardDelete(local.id)
            } catch (e: Exception) {
                dao.markSynced(local.id, SyncStatus.FAILED, local.remoteId, local.updatedAt)
            }
        }
    }

    private suspend fun pullStudents() {
        val since = prefs.lastStudentPull()?.toString()
        val remote = api.getStudentsSince(since)
        val dao = db.studentDao()

        remote.forEach { dto ->
            val localByRemote = db.query("SELECT * FROM students WHERE remoteId = ?", arrayOf(dto.id))
                .use { null }


            val local = dao.pendingOrFailed().find { it.remoteId == dto.id } ?: dao.getById(dto.id)

            val incoming = dto.toEntity(localId = local?.id)

            if (local == null) {
                if (!incoming.deleted) dao.upsert(incoming) else Unit
            } else {
                val localNewer = local.updatedAt > incoming.updatedAt && !local.deleted
                if (!localNewer) {
                    if (incoming.deleted) dao.hardDelete(local.id)
                    else dao.upsert(incoming.copy(syncStatus = SyncStatus.SYNCED))
                }
            }
        }
        prefs.setLastStudentPull(Clock.System.now())
    }

    private suspend fun pullScoreCards() {
        val since = prefs.lastScorePull()?.toString()
        val remote = api.getScoreCardsSince(since)
        val sDao = db.studentDao()
        val cDao = db.scoreCardDao()

        remote.forEach { dto ->
            val localStudentId = sDao.pendingOrFailed().find { it.remoteId == dto.studentId }?.id
                ?: sDao.getById(dto.studentId)?.id
                ?: return@forEach

            val localCandidateList = cDao.pendingOrFailed()
            val local = localCandidateList.find { it.remoteId == dto.id }

            val incoming = dto.toEntity(localId = local?.id, localStudentId = localStudentId)
            if (local == null) {
                if (!incoming.deleted) cDao.upsert(incoming) else Unit
            } else {
                val localNewer = local.updatedAt > incoming.updatedAt && !local.deleted
                if (!localNewer) {
                    if (incoming.deleted) cDao.hardDelete(local.id)
                    else cDao.upsert(incoming.copy(syncStatus = SyncStatus.SYNCED))
                }
            }
        }
        prefs.setLastScorePull(Clock.System.now())
    }
}

