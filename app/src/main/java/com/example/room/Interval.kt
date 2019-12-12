package com.example.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

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
) : Serializable {
    /**
     * Enum representing the two types of intervals
     */
    enum class IntervalType(val value: String){ REST("REST"), ACTIVE("ACTIVE") }
}