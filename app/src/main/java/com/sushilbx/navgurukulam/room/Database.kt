package com.sushilbx.navgurukulam.room
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context

@Database(entities = [Student::class, ScoreCard::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDao
    abstract fun scoreCardDao(): ScoreCardDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun get(ctx: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(ctx, AppDatabase::class.java, "app.db")
                    .fallbackToDestructiveMigration() // change to proper migrations later
                    .build().also { INSTANCE = it }
            }
    }
}
