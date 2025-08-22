package com.sushilbx.navgurukulam.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sushilbx.navgurukulam.databinding.ItemStudentBinding
import com.sushilbx.navgurukulam.room.Student
import com.sushilbx.navgurukulam.room.SyncStatus

class StudentAdapter(
    private val onRetryClick: (Student) -> Unit,
    private val onDeleteClick: (Student) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    private val students = mutableListOf<Student>()

    fun submitList(newList: List<Student>) {
        students.clear()
        students.addAll(newList)
        notifyDataSetChanged()
    }

    inner class StudentViewHolder(
        private val binding: ItemStudentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(student: Student) = with(binding) {
            tvFullName.text = student.fullName
            tvClassGender.text = "${student.className} | ${student.gender}"
            tvSchoolId.text = "School ID: ${student.schoolId}"

            when (student.syncStatus) {
                SyncStatus.SYNCED -> {
                    tvSyncStatus.text = "SYNCED"
                    tvSyncStatus.setTextColor(Color.GREEN)
                    btnRetry.visibility = View.GONE
                }
                SyncStatus.PENDING -> {
                    tvSyncStatus.text = "PENDING"
                    tvSyncStatus.setTextColor(Color.YELLOW)
                    btnRetry.visibility = View.GONE
                }
                SyncStatus.FAILED -> {
                    tvSyncStatus.text = "FAILED"
                    tvSyncStatus.setTextColor(Color.RED)
                    btnRetry.visibility = View.VISIBLE
                }
            }

            btnRetry.setOnClickListener { onRetryClick(student) }
            btnDelete.setOnClickListener { onDeleteClick(student) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding = ItemStudentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(students[position])
    }

    override fun getItemCount(): Int = students.size
}
