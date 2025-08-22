package com.sushilbx.navgurukulam

import com.sushilbx.navgurukulam.room.Student
import com.sushilbx.navgurukulam.room.StudentDao
import com.sushilbx.navgurukulam.room.SyncStatus

class StudentRepository(
    private val studentDao: StudentDao
) {

    fun getAllStudents() = studentDao.getAllStudents() // Flow<List<Student>>

    suspend fun insert(student: Student) = studentDao.insert(student)

    suspend fun delete(student: Student) = studentDao.delete(student)

    suspend fun update(student: Student) = studentDao.update(student)
    suspend fun syncPendingStudents() {
        val pending = studentDao.getPendingStudents()
        if (pending.isNotEmpty()) {
            try {
                // Push all students to server at once
             //   apiService.syncStudents(pending)

                // Mark all as synced
                pending.forEach { student ->
                    studentDao.update(student.copy(syncStatus = SyncStatus.SYNCED))
                }
            } catch (e: Exception) {
                // Mark all as failed
                pending.forEach { student ->
                    studentDao.update(student.copy(syncStatus = SyncStatus.FAILED))
                }
            }
        }
    }
}
