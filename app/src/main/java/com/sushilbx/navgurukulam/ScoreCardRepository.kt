package com.sushilbx.navgurukulam

import com.sushilbx.navgurukulam.room.ScoreCard
import com.sushilbx.navgurukulam.room.ScoreCardDao
import com.sushilbx.navgurukulam.room.SyncStatus

class ScoreCardRepository(
    private val scoreCardDao: ScoreCardDao,
    private val apiService: ApiService
) {

    fun getScoreCardsForStudent(studentId: String) =
        scoreCardDao.getScoreCardsForStudent(studentId)

    suspend fun insert(scoreCard: ScoreCard) = scoreCardDao.insert(scoreCard)

    suspend fun delete(scoreCard: ScoreCard) = scoreCardDao.delete(scoreCard)

    suspend fun update(scoreCard: ScoreCard) = scoreCardDao.update(scoreCard)
    suspend fun syncPendingScoreCards() {
        val pending = scoreCardDao.getPendingScoreCards()
        if (pending.isNotEmpty()) {
            try {
                apiService.syncScoreCards(pending) // send list

                // mark all as synced
                pending.forEach { scoreCard ->
                    scoreCardDao.update(scoreCard.copy(syncStatus = SyncStatus.SYNCED))
                }
            } catch (e: Exception) {
                // mark all as failed
                pending.forEach { scoreCard ->
                    scoreCardDao.update(scoreCard.copy(syncStatus = SyncStatus.FAILED))
                }
            }
        }
    }
}

