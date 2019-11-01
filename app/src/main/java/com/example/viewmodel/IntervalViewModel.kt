package com.example.viewmodel

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.room.AppDatabase
import com.example.room.Interval
import com.example.room.IntervalDao

/**
 * ViewModel to connect interval data to the view
 *
 * @property intervalDao Reference to the singleton database's interval Dao
 */
class IntervalViewModel(app: Application): AndroidViewModel(app) {
    private var intervalDao: IntervalDao

    init {
        val database = AppDatabase.getInstance(app)
        intervalDao = database.intervalDao()
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