package com.example.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * [Entity] data class representing the Interval table in the app database
 *
 * @property intervalId Unique, auto-incrementing interval id
 * @property intervalName Name of the interval
 * @property intervalType Type of the interval (reset or active)
 * @property intervalTime Length of interval in seconds
 * @property intervalReps Length of interval in reps
 * @property workoutId Id of the associated [Workout]
 */
@Entity
data class Interval(
    @PrimaryKey(autoGenerate=true) val intervalId: Int,
    @ColumnInfo(name="i_name") val intervalName: String,
    @ColumnInfo(name="i_type") val intervalType: String,
    @ColumnInfo(name="i_time") val intervalTime: Int?,
    @ColumnInfo(name="i_reps") val intervalReps: Int?,

    @ForeignKey(
        entity = Workout::class,
        parentColumns = ["workoutId"],
        childColumns = ["intervalId"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    ) val workoutId: Int
) {
    /**
     * Enum representing the two types of intervals
     */
    enum class IntervalType(val value: String){ REST("REST"), ACTIVE("ACTIVE") }
}