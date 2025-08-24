package com.sushilbx.navgurukulam.apis

data class StudentDto(
    val id: String,
    val fullName: String,
    val className: String,
    val gender: String,
    val schoolId: String,
    val updatedAt: String,
    val deleted: Boolean
)

data class ScoreCardDto(
    val id: String,
    val studentId: String,
    val subject: String,
    val score: Int,
    val updatedAt: String,
    val deleted: Boolean
)

data class PushResult(val id: String, val updatedAt: String)
