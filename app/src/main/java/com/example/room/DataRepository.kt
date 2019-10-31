package com.example.room

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData

/**
 * Data repository that allows centralized access to all data for the app
 *
 * @property workoutDao Reference to the workout Dao
 * @property intervalDao Reference to the interval Dao
 * @property allWorkouts [LiveData] reference to all workouts in the database
 */
class DataRepository(app: Application) {
    private var workoutDao: WorkoutDao
    private var intervalDao: IntervalDao

    private var allWorkouts: LiveData<List<Workout>>

    /**
     * Get an instance of the database and initialize the Daos and live data
     */
    init {
        val database = AppDatabase.getInstance(app)
        workoutDao = database.workoutDao()
        intervalDao = database.intervalDao()

        allWorkouts = workoutDao.getAll()
    }

    /**
     * Insert a workout to the database
     *
     * @param workout [Workout] to insert
     */
    fun insertWorkouts(vararg workout: Workout) {
        AsyncWorkoutInsert(workoutDao).execute(*workout)
    }

    /**
     * Async class to insert workouts to the database
     *
     * @param workoutDao Reference to the workout Dao
     */
    class AsyncWorkoutInsert(private val workoutDao: WorkoutDao): AsyncTask<Workout, Void, Unit>() {
        override fun doInBackground(vararg workout: Workout) {
            workoutDao.insert(*workout)
        }
    }

    /**
     * Get all workouts
     *
     * @return [LiveData] [List] of [Workout]s
     */
    fun getAllWorkouts(): LiveData<List<Workout>> {
        return this.allWorkouts
    }

    /**
     * Update a workout
     *
     * @param workout Updated [Workout] object
     */
    fun updateWorkout(workout: Workout) {
        this.workoutDao.update(workout)
    }

    /**
     * Delete a workout
     *
     * @param workout [Workout] object to delete
     */
    fun deleteWorkout(workout: Workout) {
        this.workoutDao.delete(workout)
    }

    /**
     * Insert an interval to the database
     *
     * @param interval [Interval] to insert
     */
    fun insertIntervals(vararg interval: Interval) {
        AsyncIntervalInsert(intervalDao).execute(*interval)
    }

    /**
     * Async class to insert intervals to the database
     *
     * @param intervalDao Reference to the interval Dao
     */
    class AsyncIntervalInsert(private val intervalDao: IntervalDao): AsyncTask<Interval, Void, Unit>() {
        override fun doInBackground(vararg interval: Interval) {
            intervalDao.insert(*interval)
        }
    }

    /**
     * Get all intervals in the specified workout
     *
     * @return [LiveData] [List] of [Interval]s
     */
    fun getIntervalsByWorkout(workoutId: Int): LiveData<List<Interval>> {
        return this.intervalDao.getByWorkout(workoutId)
    }

    /**
     * Update an interval
     *
     * @param interval Updated [Interval] object
     */
    fun updateInterval(interval: Interval) {
        this.intervalDao.update(interval)
    }

    /**
     * Delete an interval
     *
     * @param interval [Interval] object to delete
     */
    fun deleteInterval(interval: Interval) {
        this.intervalDao.delete(interval)
    }
}