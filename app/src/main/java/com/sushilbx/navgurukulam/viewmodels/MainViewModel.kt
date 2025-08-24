package com.sushilbx.navgurukulam.viewmodels


import android.app.Application
import androidx.lifecycle.*
import com.sushilbx.navgurukulam.StudentRepository
import com.sushilbx.navgurukulam.room.AppDatabase
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = StudentRepository(AppDatabase.get(app).studentDao())

    val students = repo.observe().asLiveData()   // ✅ still works, List<StudentWithScores>

    fun addStudent(fullName: String, className: String, gender: String, schoolId: String) =
        viewModelScope.launch { repo.add(fullName, className, gender, schoolId) }

    fun rename(id: String, newName: String) = viewModelScope.launch { repo.rename(id, newName) }
    fun delete(id: String) = viewModelScope.launch { repo.delete(id) }

    fun updateStudent(id: String, fullName: String, className: String, gender: String, schoolId: String) =
        viewModelScope.launch {
            repo.updateStudent(id, fullName, className, gender, schoolId)
        }


}



class MainVMFactory(private val app: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        MainViewModel(app) as T
}
