package com.sushilbx.navgurukulam.room

import androidx.room.TypeConverter
import kotlinx.datetime.Instant
class Converters {
    @TypeConverter
    fun fromInstant(value: Instant?): String? = value?.toString()
    @TypeConverter
    fun toInstant(value: String?): Instant? = value?.let(Instant::parse)
    @TypeConverter
    fun fromSyncStatus(v: SyncStatus?): String? = v?.name
    @TypeConverter
    fun toSyncStatus(v: String?): SyncStatus? = v?.let { SyncStatus.valueOf(it) }
}