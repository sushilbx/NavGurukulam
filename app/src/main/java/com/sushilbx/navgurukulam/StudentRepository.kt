package com.sushilbx.navgurukulam

import com.sushilbx.navgurukulam.room.Student
import com.sushilbx.navgurukulam.room.StudentDao
import com.sushilbx.navgurukulam.room.StudentWithScores
import com.sushilbx.navgurukulam.room.SyncStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class StudentRepository(
    private val dao: StudentDao
) {
    fun observe(): Flow<List<StudentWithScores>> = dao.observeAll()

    suspend fun add(fullName: String, className: String, gender: String, schoolId: String) {
        val now = Clock.System.now()
        dao.upsert(
            Student(
                fullName = fullName,
                className = className,
                gender = gender,
                schoolId = schoolId,
                updatedAt = now,
                syncStatus = SyncStatus.PENDING
            )
        )
    }

    suspend fun updateStudent(
        id: String,
        fullName: String,
        className: String,
        gender: String,
        schoolId: String
    ) {
        val existing = dao.getById(id) ?: return

        val updated = existing.copy(
            fullName = fullName,
            className = className,
            gender = gender,
            schoolId = schoolId,
            updatedAt = Clock.System.now()
        )

        dao.update(updated)
    }



    suspend fun rename(id: String, newFullName: String) {
        val now = Clock.System.now()
        val current = dao.getById(id) ?: return
        dao.upsert(
            current.copy(
                fullName = newFullName,
                updatedAt = now,
                syncStatus = SyncStatus.PENDING
            )
        )
    }

    suspend fun delete(id: String) {
        dao.softDelete(id, Clock.System.now(), SyncStatus.PENDING)
    }
}
