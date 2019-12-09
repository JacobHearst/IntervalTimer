package com.example.intervaltimer.workout

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.room.AppDatabase
import com.example.room.Workout
import com.example.room.WorkoutDao

/**
 * ViewModel to connect workout data to the view
 *
 * @property workoutDao Reference to the singleton database's workout Dao
 * @property allWorkouts Single reference to the [LiveData] representation of all workouts in the db
 */
class WorkoutViewModel(app: Application): AndroidViewModel(app) {
    private var workoutDao: WorkoutDao
    private var allWorkouts: LiveData<List<Workout>>

    init {
        val database = AppDatabase.getInstance(app)
        workoutDao = database.workoutDao()

        this.allWorkouts = workoutDao.getAll()
    }

    /**
     * Insert a workout to the database
     *
     * @param item [Workout] to insert
     */
    fun insert(vararg item: Workout) {
        AsyncWorkoutInsert(workoutDao).execute(*item)
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
    fun getAllWorkouts(): LiveData<List<Workout>> = this.allWorkouts

    /**
     * Update a workout
     *
     * @param item Updated [Workout] object
     */
    fun update(item: Workout) {
        AsyncWorkoutUpdate(workoutDao).execute(item)
    }

    /**
     * Async class to update workouts to the database
     *
     * @param workoutDao Reference to the workout Dao
     */
    class AsyncWorkoutUpdate(private val workoutDao: WorkoutDao): AsyncTask<Workout, Void, Unit>() {
        override fun doInBackground(vararg workout: Workout) {
            // This should only ever take in a single input
            workoutDao.update(workout[0])
        }
    }

    /**
     * Delete a workout
     *
     * @param item [Workout] object to delete
     */
    fun delete(item: Workout) {
        AsyncWorkoutDelete(workoutDao).execute(item)
    }

    /**
     * Async class to delete workouts from the database
     *
     * @param workoutDao Reference to the workout Dao
     */
    class AsyncWorkoutDelete(private val workoutDao: WorkoutDao): AsyncTask<Workout, Void, Unit>() {
        override fun doInBackground(vararg workout: Workout) {
            // This should only ever take in a single input
            workoutDao.delete(workout[0])
        }
    }
}