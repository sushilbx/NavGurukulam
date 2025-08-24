package com.sushilbx.navgurukulam

import com.sushilbx.navgurukulam.apis.ApiService
import com.sushilbx.navgurukulam.room.ScoreCardDao
import com.sushilbx.navgurukulam.room.SyncStatus
import com.sushilbx.navgurukulam.room.ScoreCard

class ScoreCardRepository(
    private val dao: ScoreCardDao,
    private val api: ApiService
) {
    suspend fun addScoreCard(scoreCard: ScoreCard) = dao.insert(scoreCard)
    suspend fun update(scoreCard: ScoreCard) = dao.update(scoreCard)
    suspend fun delete(scoreCard: ScoreCard) = dao.delete(scoreCard)
    suspend fun getByStudent(studentId: Long) = dao.getByStudent(studentId.toString())

    suspend fun syncPending() {
        val pending = dao.getPending()
        for (sc in pending) {
            try {
                val response = api.syncScoreCard(sc)
                if (response.isSuccessful) {
                    dao.update(sc.copy(syncStatus = SyncStatus.SYNCED))
                }
            } catch (e: Exception) {
                dao.update(sc.copy(syncStatus = SyncStatus.FAILED))
            }
        }
    }
}
