package com.sushilbx.navgurukulam.room


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import java.util.UUID


@Entity(
    tableName = "scorecards",
    foreignKeys = [ForeignKey(
        entity = Student::class,
        parentColumns = ["id"],
        childColumns = ["studentId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("studentId")]
)
data class ScoreCard(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val remoteId: String? = null,
    val studentId: String,
    val subject: String,
    val score: Int,
    val updatedAt: Instant,
    val deleted: Boolean = false,
    val syncStatus: SyncStatus = SyncStatus.PENDING,

)
