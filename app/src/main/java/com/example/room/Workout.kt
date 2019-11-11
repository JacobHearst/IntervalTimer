package com.example.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * [Entity] data class representing the Workout table in the app database
 *
 * @property id Unique, auto-incrementing workout id
 * @property name Name of the workout
 * @property length Length of the workout in minutes
 * @property isFavorite Favorite flag
 */
@Entity
data class Workout(
    @PrimaryKey(autoGenerate=true) val id: Int?,
    @ColumnInfo(name="w_name") val name: String,
    @ColumnInfo(name="w_length") val length: Double,
    @ColumnInfo(name="w_is_favorite") val isFavorite: Boolean
): Serializable