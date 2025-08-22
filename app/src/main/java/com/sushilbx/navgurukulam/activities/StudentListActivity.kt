package com.sushilbx.navgurukulam.activities

import android.os.Bundle
import android.widget.EditText
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sushilbx.navgurukulam.R
import com.sushilbx.navgurukulam.StudentRepository
import com.sushilbx.navgurukulam.adapters.StudentAdapter
import com.sushilbx.navgurukulam.databinding.ActivityStudentListBinding
import com.sushilbx.navgurukulam.room.AppDatabase
import com.sushilbx.navgurukulam.room.Student
import com.sushilbx.navgurukulam.room.SyncStatus
import com.sushilbx.navgurukulam.viewmodels.StudentViewModel
import com.sushilbx.navgurukulam.viewmodels.StudentViewModelFactory

class StudentListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentListBinding
    private lateinit var viewModel: StudentViewModel
    private lateinit var adapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.addStudentFab.setOnClickListener {
            showAddStudentDialog()
        }
        val dao = AppDatabase.getDatabase(applicationContext).studentDao()
        val repository = StudentRepository(dao)
        val factory = StudentViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[StudentViewModel::class.java]
        adapter = StudentAdapter(
            onRetryClick = { student -> viewModel.retrySync(student) },
            onDeleteClick = { student -> viewModel.deleteStudent(student) }
        )


        binding.studentRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.studentRecyclerView.adapter = adapter

        viewModel.students.observe(this) { students ->
            adapter.submitList(students)
        }
    }

    private fun showAddStudentDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_student, null)
        val nameInput = dialogView.findViewById<EditText>(R.id.editFullName)
        val classInput = dialogView.findViewById<EditText>(R.id.editClassName)
        val schoolInput = dialogView.findViewById<EditText>(R.id.editSchoolId)
        val genderGroup = dialogView.findViewById<RadioGroup>(R.id.genderGroup)

        AlertDialog.Builder(this)
            .setTitle("Add Student")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val fullName = nameInput.text.toString()
                val className = classInput.text.toString()
                val schoolId = schoolInput.text.toString()
                val gender = when (genderGroup.checkedRadioButtonId) {
                    R.id.maleRadio -> "Male"
                    R.id.femaleRadio -> "Female"
                    else -> "Other"
                }

                if (fullName.isNotBlank() && className.isNotBlank() && schoolId.isNotBlank()) {
                    viewModel.addStudent(fullName, className, gender, schoolId)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


}

