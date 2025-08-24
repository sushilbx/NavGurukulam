package com.sushilbx.navgurukulam.activities

import android.os.Bundle
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.sushilbx.navgurukulam.R
import com.sushilbx.navgurukulam.SyncScheduler
import com.sushilbx.navgurukulam.adapters.StudentAdapter
import com.sushilbx.navgurukulam.databinding.ActivityMainBinding
import com.sushilbx.navgurukulam.viewmodels.MainVMFactory
import com.sushilbx.navgurukulam.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding
    private val vm: MainViewModel by viewModels { MainVMFactory(application) }
    private val adapter = StudentAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater); setContentView(binding.root)

        binding.rvStudents.layoutManager = LinearLayoutManager(this)
        adapter.onDeleteClick = { student ->
            vm.delete(student.id)
        }
        binding.rvStudents.adapter = adapter

        vm.students.observe(this) { adapter.submitList(it) }

        binding.btnRetry.setOnClickListener {
            SyncScheduler.retryNow(this)
            Toast.makeText(this, "Syncing...", Toast.LENGTH_SHORT).show()


        }



        binding.btnAdd.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_add_student, null)
            val etFullName = dialogView.findViewById<TextInputEditText>(R.id.editFullName)
            val etClass = dialogView.findViewById<TextInputEditText>(R.id.editClassName)
            val rgGender = dialogView.findViewById<RadioGroup>(R.id.genderGroup)
            val etSchoolId = dialogView.findViewById<TextInputEditText>(R.id.editSchoolId)

            AlertDialog.Builder(this)
                .setTitle("Add Student")
                .setView(dialogView)
                .setPositiveButton("Add") { dialog, _ ->
                    val fullName = etFullName.text.toString().trim()
                    val className = etClass.text.toString().trim()
                    val gender = when (rgGender.checkedRadioButtonId) {
                        R.id.maleRadio -> "Male"
                        R.id.femaleRadio -> "Female"
                        else -> ""
                    }
                    val schoolId = etSchoolId.text.toString().trim()

                    if (fullName.isNotEmpty() && className.isNotEmpty() && gender.isNotEmpty() && schoolId.isNotEmpty()) {
                        vm.addStudent(fullName, className, gender, schoolId)
                    } else {
                        Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }


        adapter.onEditClick = { student ->
            val dialogView = layoutInflater.inflate(R.layout.dialog_add_student, null)

            val etFullName = dialogView.findViewById<TextInputEditText>(R.id.editFullName)
            val etClass = dialogView.findViewById<TextInputEditText>(R.id.editClassName)
            val rgGender = dialogView.findViewById<RadioGroup>(R.id.genderGroup)
            val etSchoolId = dialogView.findViewById<TextInputEditText>(R.id.editSchoolId)

            etFullName.setText(student.fullName)
            etClass.setText(student.className)
            etSchoolId.setText(student.schoolId)
            when (student.gender) {
                "Male" -> rgGender.check(R.id.maleRadio)
                "Female" -> rgGender.check(R.id.femaleRadio)
            }

            AlertDialog.Builder(this)
                .setTitle("Edit Student")
                .setView(dialogView)
                .setPositiveButton("Save") { dialog, _ ->
                    val fullName = etFullName.text.toString().trim()
                    val className = etClass.text.toString().trim()
                    val gender = when (rgGender.checkedRadioButtonId) {
                        R.id.maleRadio -> "Male"
                        R.id.femaleRadio -> "Female"
                        else -> ""
                    }
                    val schoolId = etSchoolId.text.toString().trim()

                    if (fullName.isNotEmpty() && className.isNotEmpty() && gender.isNotEmpty() && schoolId.isNotEmpty()) {
                        vm.updateStudent(student.id, fullName, className, gender, schoolId)
                    } else {
                        Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }


}

