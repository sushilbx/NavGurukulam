package com.sushilbx.navgurukulam.room
import androidx.room.*
import kotlinx.datetime.Instant
import java.util.UUID
@Entity(tableName = "students")
data class Student(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val remoteId: String? = null,

    val fullName: String,
    val className: String,
    val gender: String,
    val schoolId: String,
    val updatedAt: Instant,
    val deleted: Boolean = false,
    val syncStatus: SyncStatus = SyncStatus.PENDING
)
