package com.example.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Database for storing workouts and intervals
 *
 */
@Database(entities = [Workout::class, Interval::class], version = 3, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
    abstract fun intervalDao(): IntervalDao

    /**
     * Singleton pattern to avoid building the database multiple times
     */
    companion object {
        private var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_db"
                ).build()
            }

            return instance as AppDatabase
        }
    }
}