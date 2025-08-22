package com.sushilbx.navgurukulam.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fullName: String,
    val className: String,
    val gender: String,
    val schoolId: String,
    val createdAt: Long,
    val updatedAt: Long,
    val syncStatus: SyncStatus
)

@Entity(
    tableName = "scorecards",
    foreignKeys = [ForeignKey(
        entity = Student::class,
        parentColumns = ["id"],
        childColumns = ["studentId"],
        onDelete = CASCADE
    )],
    indices = [Index("studentId")]
)


data class ScoreCard(
    @PrimaryKey val id: String,
    val studentId: String,
    val subject: String,
    val score: Int,
    val createdAt: Long,
    val updatedAt: Long,
    val syncStatus: SyncStatus
)

enum class SyncStatus { SYNCED, PENDING, FAILED }
