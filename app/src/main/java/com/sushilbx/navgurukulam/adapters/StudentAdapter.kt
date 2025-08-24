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
import com.sushilbx.navgurukulam.databinding.ItemScoreBinding
import com.sushilbx.navgurukulam.databinding.ItemStudentBinding
import com.sushilbx.navgurukulam.room.ScoreCard
import com.sushilbx.navgurukulam.room.Student
import com.sushilbx.navgurukulam.room.StudentWithScores
import com.sushilbx.navgurukulam.room.SyncStatus

class StudentAdapter : ListAdapter<StudentWithScores, StudentVH>(DIFF) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        StudentVH(ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false))

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
    }



    fun bind(studentWithScores: StudentWithScores) {
        val student = studentWithScores.student
        currentStudent = student
        binding.tvName.text = student.name

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

        // ✅ Now works, because StudentWithScores has the scores list
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

        // Optional: color coding for scores
        when {
            scoreCard.score >= 75 -> tvScore.setTextColor(Color.parseColor("#2E7D32")) // green
            scoreCard.score >= 40 -> tvScore.setTextColor(Color.parseColor("#F9A825")) // yellow
            else -> tvScore.setTextColor(Color.parseColor("#C62828")) // red
        }
    }
}
