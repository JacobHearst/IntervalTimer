package com.example.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * [Entity] data class representing the Interval table in the app database
 *
 * @property id Unique, auto-incrementing interval id
 * @property name Name of the interval
 * @property type Type of the interval (reset or active)
 * @property time Length of interval in seconds
 * @property reps Length of interval in reps
 * @property color C?olor of interval in HexCode
 * @property index The index of the interval in the workout
 * @property workoutId Id of the associated [Workout]
 */
@Entity
data class Interval(
    @PrimaryKey(autoGenerate=true) val id: Int?,
    @ColumnInfo(name="i_name") var name: String,
    @ColumnInfo(name="i_type") var type: String,
    @ColumnInfo(name="i_time") var time: Int?,
    @ColumnInfo(name="i_reps") var reps: Int?,
    @ColumnInfo(name="i_color") var color: String,
    @ColumnInfo(name="i_index") var index: Int,

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

        // I changed intervalTime to be a var.
        // This caused the compiler to throw an error about the variable possibly changing.
        // So, this should fix that issue.
        val time = this.time;

        return when {
            time != null -> {
                val minutes: Int = time / 60
                val seconds = time % 60
                // Add a 0 in front of the seconds if it's < 10
                // Turns this: 1:3 to this: 1:03
                "$minutes:${if(seconds < 10) "0" else ""}$seconds"
            }
            this.reps != null -> "${this.reps} reps"
            else -> "Error"
        }
    }
}