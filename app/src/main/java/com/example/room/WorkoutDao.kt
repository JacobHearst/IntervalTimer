package com.example.room

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * A [Dao] for accessing and modifying the Workout table
 */
@Dao
interface WorkoutDao {
    /**
     * Insert a new workout into the table
     *
     * @param workout [Workout](s) to add to the table
     */
    @Insert
    fun insert(vararg workout: Workout)

    /**
     * Select all workouts from the database
     *
     * @return [LiveData] [List] of [Workout]s from the database
     */
    @Query("SELECT * FROM workout")
    fun getAll(): LiveData<List<Workout>>

    /**
     * Update a workout
     *
     * @param workout Updated [Workout] object
     */
    @Update
    fun update(workout: Workout)

    /**
     * Delete a workout
     *
     * @param workout [Workout] to delete
     */
    @Delete
    fun delete(workout: Workout)
}