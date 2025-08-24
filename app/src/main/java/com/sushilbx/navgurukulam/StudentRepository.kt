package com.sushilbx.navgurukulam

import com.sushilbx.navgurukulam.room.StudentDao
import com.sushilbx.navgurukulam.room.SyncStatus
import com.sushilbx.navgurukulam.room.Student
import com.sushilbx.navgurukulam.room.StudentWithScores

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock

class StudentRepository(
    private val dao: StudentDao
) {
    fun observe(): Flow<List<StudentWithScores>> = dao.observeAll()

    suspend fun add(name: String) {
        val now = Clock.System.now()
        dao.upsert(Student(name = name, updatedAt = now, syncStatus = SyncStatus.PENDING))
    }

    suspend fun rename(id: String, newName: String) {
        val now = Clock.System.now()
        val current = dao.getById(id) ?: return
        dao.upsert(current.copy(name = newName, updatedAt = now, syncStatus = SyncStatus.PENDING))
    }

    suspend fun delete(id: String) {
        dao.softDelete(id, Clock.System.now(), SyncStatus.PENDING)
    }
}
