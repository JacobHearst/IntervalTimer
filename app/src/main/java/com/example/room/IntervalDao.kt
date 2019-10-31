package com.example.room

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * A [Dao] for accessing and modifying the Interval table
 */
@Dao
interface IntervalDao {
    /**
     * Insert a new interval into the table
     *
     * @param interval [Interval](s) to add to the table
     */
    @Insert
    fun insert(vararg interval: Interval)

    /**
     * Select all intervals from a workout
     *
     * @param workoutId Id of the workout to select intervals from
     * @return [LiveData] [List] of [Interval]s from the desired workout
     */
    @Query("SELECT * FROM interval WHERE workoutId = :workoutId")
    fun getByWorkout(workoutId: Int): LiveData<List<Interval>>

    /**
     * Update an interval
     *
     * @param interval Updated [Interval] object
     */
    @Update
    fun update(interval: Interval)

    /**
     * Delete an interval from the table
     *
     * @param interval Interval to delete
     */
    @Delete
    fun delete(interval: Interval)
}