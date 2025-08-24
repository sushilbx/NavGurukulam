package com.sushilbx.navgurukulam.apis

import com.sushilbx.navgurukulam.room.SyncStatus
import com.sushilbx.navgurukulam.room.ScoreCard
import com.sushilbx.navgurukulam.room.Student
import kotlinx.datetime.Instant

fun Student.toDto(): StudentDto = StudentDto(
    id = remoteId ?: "", name = name, updatedAt = updatedAt.toString(), deleted = deleted
)

fun ScoreCard.toDto(remoteStudentId: String): ScoreCardDto = ScoreCardDto(
    id = remoteId ?: "", studentId = remoteStudentId, subject = subject, score = score,
    updatedAt = updatedAt.toString(), deleted = deleted
)

fun StudentDto.toEntity(localId: String? = null): Student = Student(
    id = localId ?: id, remoteId = id, name = name, updatedAt = Instant.parse(updatedAt),
    deleted = deleted, syncStatus = SyncStatus.SYNCED
)

fun ScoreCardDto.toEntity(localId: String? = null, localStudentId: String): ScoreCard = ScoreCard(
    id = localId ?: id, remoteId = id, studentId = localStudentId, subject = subject, score = score,
    updatedAt = Instant.parse(updatedAt), deleted = deleted, syncStatus = SyncStatus.SYNCED
)
