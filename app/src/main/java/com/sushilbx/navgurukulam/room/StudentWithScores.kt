package com.sushilbx.navgurukulam.room

import androidx.room.Embedded
import androidx.room.Relation

data class StudentWithScores(
    @Embedded val student: Student,
    @Relation(
        parentColumn = "id",
        entityColumn = "studentId"
    )
    val scores: List<ScoreCard>
)
