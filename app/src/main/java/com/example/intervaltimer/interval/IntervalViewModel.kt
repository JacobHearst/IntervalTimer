package com.example.intervaltimer.interval

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
     * @param item [Interval] to insert
     */
    fun insert(vararg item: Interval) {
        AsyncIntervalInsert(intervalDao).execute(*item)
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
     * @param item Updated [Interval] object
     */
    fun update(item: Interval) {
        AsyncIntervalUpdate(intervalDao).execute(item)
    }

    /**
     * Async class to insert intervals to the database
     *
     * @param intervalDao Reference to the interval Dao
     */
    class AsyncIntervalUpdate(private val intervalDao: IntervalDao): AsyncTask<Interval, Void, Unit>() {
        override fun doInBackground(vararg interval: Interval) {
            intervalDao.update(*interval)
        }
    }

    /**
     * Delete an interval
     *
     * @param item [Interval] object to delete
     */
    fun delete(item: Interval) {
        AsyncIntervalDelete(intervalDao).execute(item)
    }

    /**
     * Async class to insert intervals to the database
     *
     * @param intervalDao Reference to the interval Dao
     */
    class AsyncIntervalDelete(private val intervalDao: IntervalDao): AsyncTask<Interval, Void, Unit>() {
        override fun doInBackground(vararg interval: Interval) {
            // This should only ever receive a single interval
            intervalDao.delete(interval[0])
        }
    }
}