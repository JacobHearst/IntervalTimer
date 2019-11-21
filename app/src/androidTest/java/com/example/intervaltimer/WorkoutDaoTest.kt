package com.example.intervaltimer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.intervaltimer.util.OneTimeObserver
import com.example.room.AppDatabase
import com.example.room.Workout
import com.example.room.WorkoutDao
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Testing class for validating the Workout database access operations
 *
 * NOTE: The getAll() method doesn't have its own unit test because every other test uses it to
 * verify that the other operations were executed. Adding a test for it would essentially just
 * be copying and pasting one of the insert tests
 *
 * @property mockDatabase Reference to an in-memory database
 * @property mockWorkoutDao Reference to the mock database's workout Dao
 */
@RunWith(AndroidJUnit4::class)
class WorkoutDaoTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var mockDatabase: AppDatabase
    private lateinit var mockWorkoutDao: WorkoutDao

    /**
     * Extension function to allow tests involving [LiveData]
     */
    private fun <T> LiveData<T>.observeOnce(onChangeHandler: (T) -> Unit) {
        val observer = OneTimeObserver(handler = onChangeHandler)
        observe(observer, observer)
    }

    /**
     * Initialize the mock database in memory and initialize Dao references
     */
    @Before
    fun init() {
        mockDatabase = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AppDatabase::class.java
        ).build()

        mockWorkoutDao = mockDatabase.workoutDao()
    }

    /**
     * Clean up the database
     */
    @After
    fun closeDb() {
        mockDatabase.close()
    }

    /**
     * Validate single workout insert
     */
    @Test
    fun singleInsert() {
        val workout = Workout(null, "Test workout", 300, false)
        mockWorkoutDao.insert(workout)

        mockWorkoutDao.getAll().observeOnce {
            assert(it.size == 1)
        }
    }

    /**
     * Validate multiple workout insert
     */
    @Test
    fun multipleInsert() {
        val workouts = arrayOf(
            Workout(null, "Test workout", 300, false),
            Workout(null, "Test workout 2", 450, true),
            Workout(null, "Test workout 3", 60, false),
            Workout(null, "Test workout 4", 25, true)
        )
        mockWorkoutDao.insert(*workouts)

        mockWorkoutDao.getAll().observeOnce {
            assert(it.size == 4)
        }
    }

    /**
     * Validate workout update
     */
    @Test
    fun update() {
        // Insert a workout so we have something to update
        val workout = Workout(null, "Test workout", 300, false)
        mockWorkoutDao.insert(workout)

        // We know that the id is 1 because it's the first element inserted in the database
        val updatedWorkout = Workout(1, "Updated workout", 400, false)
        mockWorkoutDao.update(updatedWorkout)

        mockWorkoutDao.getAll().observeOnce {
            assert(it[0].name == updatedWorkout.name)
        }
    }

    /**
     * Validate workout deletion
     */
    @Test
    fun delete() {
        // Insert a workout so we have something to delete
        val workout = Workout(null, "Test workout", 300, false)
        mockWorkoutDao.insert(workout)

        mockWorkoutDao.delete(workout)
        mockWorkoutDao.getAll().observeOnce {
            assert(it.isEmpty())
        }
    }
}