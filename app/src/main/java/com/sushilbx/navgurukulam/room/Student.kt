package com.sushilbx.navgurukulam.room
import androidx.room.*
import kotlinx.datetime.Instant
import java.util.UUID

@Entity(tableName = "students")
data class Student(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val remoteId: String? = null,                      // server id
    val name: String,
    val updatedAt: Instant,                             // last local change time
    val deleted: Boolean = false,                       // soft delete for sync
    val syncStatus: SyncStatus = SyncStatus.PENDING
)

