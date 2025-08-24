package com.sushilbx.navgurukulam.adapters

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sushilbx.navgurukulam.R
import com.sushilbx.navgurukulam.activities.AddScoreActivity
import com.sushilbx.navgurukulam.databinding.ItemStudentBinding
import com.sushilbx.navgurukulam.room.ScoreCard
import com.sushilbx.navgurukulam.room.Student
import com.sushilbx.navgurukulam.room.StudentWithScores
import com.sushilbx.navgurukulam.room.SyncStatus
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.core.graphics.toColorInt

class StudentAdapter : ListAdapter<StudentWithScores, StudentVH>(DIFF) {

    var onDeleteClick: ((Student) -> Unit)? = null
    var onEditClick: ((Student) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentVH {
        val holder = StudentVH(
            ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
        holder.onDeleteClick = onDeleteClick
        holder.onEditClick = onEditClick
        return holder
    }

    override fun onBindViewHolder(holder: StudentVH, position: Int) =
        holder.bind(getItem(position))

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<StudentWithScores>() {
            override fun areItemsTheSame(old: StudentWithScores, new: StudentWithScores) =
                old.student.id == new.student.id

            override fun areContentsTheSame(old: StudentWithScores, new: StudentWithScores) =
                old == new
        }
    }
}

class StudentVH(private val binding: ItemStudentBinding) : RecyclerView.ViewHolder(binding.root) {
    private val scoreAdapter = ScoreAdapter()
    private var currentStudent: Student? = null
    var onDeleteClick: ((Student) -> Unit)? = null
    var onEditClick: ((Student) -> Unit)? = null


    init {
        binding.rvScores.layoutManager =
            LinearLayoutManager(binding.root.context, RecyclerView.VERTICAL, false)
        binding.rvScores.adapter = scoreAdapter

        binding.btnAddScore.setOnClickListener {
            currentStudent?.let { student ->
                val context = it.context
                val intent = Intent(context, AddScoreActivity::class.java)
                intent.putExtra("studentId", student.id)
                context.startActivity(intent)
            }
        }

        binding.ivDelete.setOnClickListener {
            currentStudent?.let { student ->
                onDeleteClick?.invoke(student)
            }
        }

        binding.mbEdit.setOnClickListener {
            currentStudent?.let { student ->
                onEditClick?.invoke(student)
            }
        }
    }


    fun bind(studentWithScores: StudentWithScores) {
        val student = studentWithScores.student
        currentStudent = student
        binding.tvName.text = student.fullName
        binding.tvClass.text = student.className
        binding.tvGender.text = student.gender
        binding.tvSchoolid.text = student.schoolId
        val formatter =
            DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a", Locale.getDefault())

        val instant = Instant.parse(student.updatedAt.toString())
        binding.tvLastUpdated.text ="Last updated : " +instant.atZone(ZoneId.systemDefault()).format(formatter)

        when (student.syncStatus) {
            SyncStatus.SYNCED -> {
                binding.tvSync.text = "SYNCED"
                binding.tvSync.setBackgroundColor(Color.parseColor("#2E7D32"))
            }

            SyncStatus.PENDING -> {
                binding.tvSync.text = "PENDING"
                binding.tvSync.setBackgroundColor(Color.parseColor("#FFA000"))
            }

            SyncStatus.FAILED -> {
                binding.tvSync.text = "FAILED"
                binding.tvSync.setBackgroundColor(Color.parseColor("#C62828"))
            }
        }


        scoreAdapter.submitList(studentWithScores.scores)
    }

}

class ScoreAdapter : ListAdapter<ScoreCard, ScoreVH>(DIFF) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_score, parent, false)
        return ScoreVH(view)
    }

    override fun onBindViewHolder(holder: ScoreVH, position: Int) =
        holder.bind(getItem(position))

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<ScoreCard>() {
            override fun areItemsTheSame(old: ScoreCard, new: ScoreCard) = old.id == new.id
            override fun areContentsTheSame(old: ScoreCard, new: ScoreCard) = old == new
        }
    }
}

class ScoreVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvSubject: TextView = itemView.findViewById(R.id.tvSubject)
    private val tvScore: TextView = itemView.findViewById(R.id.tvScore)

    fun bind(scoreCard: ScoreCard) {
        tvSubject.text = scoreCard.subject
        tvScore.text = scoreCard.score.toString()
        when {
            scoreCard.score >= 75 -> tvScore.setTextColor("#2E7D32".toColorInt())
            scoreCard.score >= 40 -> tvScore.setTextColor("#F9A825".toColorInt())
            else -> tvScore.setTextColor("#C62828".toColorInt())
        }
    }
}
