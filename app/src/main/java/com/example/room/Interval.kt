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
 * @property index The index of the interval in the workout
 * @property workoutId Id of the associated [Workout]
 */
@Entity
data class Interval(
    @PrimaryKey(autoGenerate=true) val intervalId: Int?,
    @ColumnInfo(name="i_name") var intervalName: String,
    @ColumnInfo(name="i_type") var intervalType: String,
    @ColumnInfo(name="i_time") var intervalTime: Int?,
    @ColumnInfo(name="i_reps") var intervalReps: Int?,
    @ColumnInfo(name="i_index") var index: Int?,

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

    /**
     * Determine whether to display the interval duration as a time or
     * as the number of reps. If displaying as time, ensure that the time
     * is properly formatted
     *
     * @return String representation of the duration of the interval
     */
    fun getDurationLabel(): String {
        return when {
            this.intervalTime != null -> {
                val minutes: Int = this.intervalTime!! / 60
                val seconds = this.intervalTime!! % 60
                // Add a 0 in front of the seconds if it's < 10
                // Turns this: 1:3 to this: 1:03
                "$minutes:${if(seconds < 10) "0" else ""}$seconds"
            }
            this.intervalReps != null -> "${this.intervalReps} reps"
            else -> "Error"
        }
    }
}