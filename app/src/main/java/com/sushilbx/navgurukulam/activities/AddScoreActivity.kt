package com.sushilbx.navgurukulam.activities

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.sushilbx.navgurukulam.R
import com.sushilbx.navgurukulam.room.AppDatabase
import com.sushilbx.navgurukulam.room.ScoreCard
import com.sushilbx.navgurukulam.room.ScoreCardDao
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class AddScoreActivity : AppCompatActivity() {
    private lateinit var dao: ScoreCardDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_score)

        dao = AppDatabase.get(this).scoreCardDao()
        val studentId = intent.getStringExtra("studentId") ?: return

        val etSubject = findViewById<TextInputEditText>(R.id.etSubject)
        val etMarks = findViewById<TextInputEditText>(R.id.etMarks)
        val btnSave = findViewById<Button>(R.id.btnSave)

        btnSave.setOnClickListener {
            val subject = etSubject.text.toString()
            val marks = etMarks.text.toString().toIntOrNull() ?: 0

            lifecycleScope.launch {
                dao.insert(
                    ScoreCard(
                        studentId = studentId,
                        subject = subject,
                        score = marks,
                        updatedAt = Clock.System.now()
                    )
                )
                finish()
            }
        }
    }
}
