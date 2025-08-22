package com.sushilbx.navgurukulam.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.sushilbx.navgurukulam.StudentRepository
import com.sushilbx.navgurukulam.room.Student
import com.sushilbx.navgurukulam.room.SyncStatus
import kotlinx.coroutines.launch
class StudentViewModel(private val repository: StudentRepository) : ViewModel() {

    val students: LiveData<List<Student>> = repository.getAllStudents().asLiveData()

    fun addStudent(fullName: String, className: String, gender: String, schoolId: String) {
        val now = System.currentTimeMillis()
        val student = Student(
            fullName = fullName,
            className = className,
            gender = gender,
            schoolId = schoolId,
            createdAt = now,
            updatedAt = now,
            syncStatus = SyncStatus.PENDING
        )
        viewModelScope.launch {
            repository.insert(student)
        }
    }

    fun deleteStudent(student: Student) = viewModelScope.launch { repository.delete(student) }

    fun retrySync(student: Student) = viewModelScope.launch {
        repository.update(student.copy(syncStatus = SyncStatus.PENDING, updatedAt = System.currentTimeMillis()))
    }
}
