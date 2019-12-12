package com.example.intervaltimer


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.intervaltimer.workout.WorkoutCardAdapter
import com.google.android.material.snackbar.Snackbar
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class WorkoutAddDeleteTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
        val adapter = mActivityTestRule.activity.findViewById<RecyclerView>(R.id.cardRecyclerView).adapter as WorkoutCardAdapter
        adapter.deleteAllItems()
    }

    @Test
    fun workoutAddDeleteTest() {
        val floatingActionButton = onView(
            allOf(
                withId(R.id.addWorkoutButton),
                childAtPosition(
                    allOf(
                        withId(R.id.frameLayout),
                        childAtPosition(
                            withId(R.id.navHostFragment),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        floatingActionButton.perform(click())

        val appCompatEditText = onView(
            allOf(
                withId(R.id.workoutNameEditText),
                childAtPosition(
                    allOf(
                        withId(R.id.workoutModalConstraintView),
                        childAtPosition(
                            withId(android.R.id.custom),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText("test 1"), closeSoftKeyboard())

        val appCompatButton = onView(
            allOf(
                withId(android.R.id.button1), withText("Submit"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        )
        appCompatButton.perform(scrollTo(), click())

        val floatingActionButton2 = onView(
            allOf(
                withId(R.id.addWorkoutButton),
                childAtPosition(
                    allOf(
                        withId(R.id.frameLayout),
                        childAtPosition(
                            withId(R.id.navHostFragment),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        floatingActionButton2.perform(click())

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.workoutNameEditText),
                childAtPosition(
                    allOf(
                        withId(R.id.workoutModalConstraintView),
                        childAtPosition(
                            withId(android.R.id.custom),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(replaceText("test 2"), closeSoftKeyboard())

        val appCompatButton2 = onView(
            allOf(
                withId(android.R.id.button1), withText("Submit"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        )
        appCompatButton2.perform(scrollTo(), click())

        val floatingActionButton3 = onView(
            allOf(
                withId(R.id.addWorkoutButton),
                childAtPosition(
                    allOf(
                        withId(R.id.frameLayout),
                        childAtPosition(
                            withId(R.id.navHostFragment),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        floatingActionButton3.perform(click())

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.workoutNameEditText),
                childAtPosition(
                    allOf(
                        withId(R.id.workoutModalConstraintView),
                        childAtPosition(
                            withId(android.R.id.custom),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText3.perform(replaceText("test 3"), closeSoftKeyboard())

        val appCompatButton3 = onView(
            allOf(
                withId(android.R.id.button1), withText("Submit"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        )
        appCompatButton3.perform(scrollTo(), click())

        onView(childAtPosition(withId(R.id.cardRecyclerView), 0)).perform(swipeLeft())

        Thread.sleep(100)

        println("TEST 1")

        val snackbarButton = onView(allOf(withId(R.id.snackbar_action), withText("Undo")))
        snackbarButton.perform(click())


        onView(childAtPosition(withId(R.id.cardRecyclerView), 0)).perform(swipeLeft())

        Thread.sleep(100)

        println("TEST 2")
        onView(childAtPosition(withId(R.id.cardRecyclerView), 1)).perform(swipeLeft())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
