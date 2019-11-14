package com.example.intervaltimer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.intervaltimer.util.OneTimeObserver
import com.example.room.AppDatabase
import com.example.room.Interval
import com.example.room.IntervalDao
import com.example.room.Workout
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Testing class for validating the Interval database access operations
 *
 * NOTE: The getByWorkout() method doesn't have its own unit test because every other test uses it
 * to verify that the other operations were executed. Adding a test for it would essentially just
 * be copying and pasting one of the insert tests
 *
 * @property mockDatabase Reference to an in-memory database
 * @property mockIntervalDao Reference to the mock database's interval Dao
 */
@RunWith(AndroidJUnit4::class)
class IntervalDaoTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var mockDatabase: AppDatabase
    private lateinit var mockIntervalDao: IntervalDao

    /**
     * Extension function to allow tests involving [LiveData]
     */
    private fun <T> LiveData<T>.observeOnce(onChangeHandler: (T) -> Unit) {
        val observer = OneTimeObserver(handler = onChangeHandler)
        observe(observer, observer)
    }

    /**
     * Initialize the mock database in memory, initialize Dao references, and insert mock workouts
     */
    @Before
    fun init() {
        mockDatabase = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AppDatabase::class.java
        ).build()

        mockIntervalDao = mockDatabase.intervalDao()

        val mockWorkouts = arrayOf(
            Workout(1, "Workout 1", 300, false),
            Workout(2, "Workout 2", 450, true),
            Workout(3, "Workout 3", 60, false),
            Workout(4, "Workout 4", 25, true)
        )
        mockDatabase.workoutDao().insert(*mockWorkouts)
    }

    /**
     * Clean up the database
     */
    @After
    fun closeDb() {
        mockDatabase.close()
    }

    /**
     * Validate single interval insert
     */
    @Test
    fun singleInsert() {
        val interval = Interval(
            null,
            "Test interval",
            Interval.IntervalType.ACTIVE.value,
            300,
            null,
            "0000",
            0,
            123
        )

        mockIntervalDao.insert(interval)

        mockIntervalDao.getByWorkout(1).observeOnce {
            assert(it.size == 1)
        }
    }

    /**
     * Validate multiple interval insert
     */
    @Test
    fun multipleInsert() {
        val interval1 = Interval(
            null,
            "Interval 1",
            Interval.IntervalType.ACTIVE.value,
            300,
            null,
            "000",
            0,
            123
        )

        val interval2 = Interval(
            null,
            "Interval 2",
            Interval.IntervalType.REST.value,
            1000000,
            null,
            "000",
            1,
            345
        )

        val interval3 = Interval(
            null,
            "Interval 3",
            Interval.IntervalType.ACTIVE.value,
            null,
            20,
            "002",
            2,
            453
        )
        mockIntervalDao.insert(interval1, interval2, interval3)

        mockIntervalDao.getByWorkout(1).observeOnce {
            assert(it.size == 2)
        }
    }

    /**
     * Validate interval update
     */
    @Test
    fun update() {
        // Insert an interval so we have something to update
        val interval = Interval(
            null,
            "Interval 1",
            Interval.IntervalType.ACTIVE.value,
            300,
            null,
            "0005",
            3,
            864
        )
        mockIntervalDao.insert(interval)

        // We know that the id is 1 because it's the first element inserted in the database
        val updatedInterval = Interval(
            1,
            "Updated interval 1",
            Interval.IntervalType.REST.value,
            null,
            30,
            "304",
            4,
            9382
        )

        mockIntervalDao.getByWorkout(1).observeOnce {
            assert(it[0].name == updatedInterval.name)
            assert(it[0].time == updatedInterval.time)
            assert(it[0].reps == updatedInterval.reps)
        }
    }

    /**
     * Validate interval deletion
     */
    @Test
    fun delete() {
        // Insert a workout so we have something to delete
        val interval = Interval(
            null,
            "Interval 1",
            Interval.IntervalType.ACTIVE.value,
            300,
            null,
            "abc",
            0,
            3222
        )
        mockIntervalDao.insert(interval)

        mockIntervalDao.delete(interval)
        mockIntervalDao.getByWorkout(1).observeOnce {
            assert(it.isEmpty())
        }
    }
}