package com.example.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * [Entity] data class representing the Workout table in the app database
 *
 * @property workoutId Unique, auto-incrementing workout id
 * @property workoutName Name of the workout
 * @property workoutLength Length of the workout in seconds
 */
@Entity
data class Workout(
    @PrimaryKey(autoGenerate=true) val workoutId: Int?,
    @ColumnInfo(name="w_name") val workoutName: String,
    @ColumnInfo(name="w_length") val workoutLength: Int
)