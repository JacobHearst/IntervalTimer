package com.example.intervaltimer


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class IntervalAddDeleteTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun intervalAddDeleteTest() {
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
        appCompatEditText.perform(replaceText("h"), closeSoftKeyboard())

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

        val linearLayout = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.cardRecyclerView),
                        childAtPosition(
                            withId(R.id.frameLayout),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        linearLayout.perform(click())

        val textView = onView(
            allOf(
                withId(R.id.intervalViewTotalTime), withText("0:00"),
                childAtPosition(
                    allOf(
                        withId(R.id.intervalViewHeading),
                        childAtPosition(
                            IsInstanceOf.instanceOf(android.view.ViewGroup::class.java),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("0:00")))

        val textView2 = onView(
            allOf(
                withId(R.id.intervalViewWorkoutName), withText("h"),
                childAtPosition(
                    allOf(
                        withId(R.id.intervalViewHeading),
                        childAtPosition(
                            IsInstanceOf.instanceOf(android.view.ViewGroup::class.java),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("h")))

        val textView3 = onView(
            allOf(
                withId(R.id.intervalZeroStateLabel),
                withText("Click the plus button to add an interval"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.navHostFragment),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("Click the plus button to add an interval")))

        val appCompatButton2 = onView(
            allOf(
                withId(R.id.startWorkout), withText("Start"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.navHostFragment),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatButton2.perform(click())

        val floatingActionButton2 = onView(
            allOf(
                withId(R.id.addInterval),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.navHostFragment),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        floatingActionButton2.perform(click())

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.interval_name_input),
                withContentDescription("Input for the Interval name"),
                childAtPosition(
                    allOf(
                        withId(R.id.interval_modal_constraint_layout),
                        childAtPosition(
                            withId(android.R.id.custom),
                            0
                        )
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(replaceText("e"), closeSoftKeyboard())

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.minutes_input),
                childAtPosition(
                    allOf(
                        withId(R.id.units_input_constraint_layout),
                        childAtPosition(
                            withId(R.id.interval_modal_constraint_layout),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText3.perform(replaceText("11"), closeSoftKeyboard())

        val appCompatButton3 = onView(
            allOf(
                withId(android.R.id.button1), withText("Add"),
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

        val textView4 = onView(
            allOf(
                withId(R.id.intervalName), withText("e"),
                childAtPosition(
                    allOf(
                        withId(R.id.constraintLayout),
                        childAtPosition(
                            IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textView4.check(matches(withText("e")))

        val textView5 = onView(
            allOf(
                withId(R.id.intervalTime), withText("11:00"),
                childAtPosition(
                    allOf(
                        withId(R.id.constraintLayout),
                        childAtPosition(
                            IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        textView5.check(matches(withText("11:00")))

        val textView6 = onView(
            allOf(
                withId(R.id.intervalViewTotalTime), withText("11:00"),
                childAtPosition(
                    allOf(
                        withId(R.id.intervalViewHeading),
                        childAtPosition(
                            IsInstanceOf.instanceOf(android.view.ViewGroup::class.java),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        textView6.check(matches(withText("11:00")))

        val floatingActionButton3 = onView(
            allOf(
                withId(R.id.addInterval),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.navHostFragment),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        floatingActionButton3.perform(click())

        val appCompatEditText4 = onView(
            allOf(
                withId(R.id.interval_name_input),
                withContentDescription("Input for the Interval name"),
                childAtPosition(
                    allOf(
                        withId(R.id.interval_modal_constraint_layout),
                        childAtPosition(
                            withId(android.R.id.custom),
                            0
                        )
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        appCompatEditText4.perform(replaceText("l"), closeSoftKeyboard())

        val appCompatRadioButton = onView(
            allOf(
                withId(R.id.reps_radio_button), withText("Reps"),
                childAtPosition(
                    allOf(
                        withId(R.id.workout_units_group),
                        childAtPosition(
                            withId(R.id.interval_modal_constraint_layout),
                            3
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatRadioButton.perform(click())

        val appCompatEditText5 = onView(
            allOf(
                withId(R.id.reps_input),
                childAtPosition(
                    allOf(
                        withId(R.id.units_input_constraint_layout),
                        childAtPosition(
                            withId(R.id.interval_modal_constraint_layout),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText5.perform(replaceText("2"), closeSoftKeyboard())

        val appCompatButton4 = onView(
            allOf(
                withId(android.R.id.button1), withText("Add"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        )
        appCompatButton4.perform(scrollTo(), click())

        onView(childAtPosition(withId(R.id.intervalList), 1)).perform(swipeLeft())

        val snackbarButton = onView(allOf(withId(R.id.snackbar_action), withText("Undo")))
        snackbarButton.perform(click())

        onView(childAtPosition(withId(R.id.intervalList), 0)).perform(swipeLeft())
        snackbarButton.perform(click())

        onView(childAtPosition(withId(R.id.intervalList), 1)).perform(swipeUp())
        onView(childAtPosition(withId(R.id.intervalList), 0)).perform(swipeLeft())
        snackbarButton.perform(click())

        onView(childAtPosition(withId(R.id.intervalList), 0)).perform(swipeLeft())
        onView(childAtPosition(withId(R.id.intervalList), 0)).perform(swipeLeft())

        val textView7 = onView(
            allOf(
                withId(R.id.intervalZeroStateLabel),
                withText("Click the plus button to add an interval"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.navHostFragment),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        textView7.check(matches(isDisplayed()))

        val textView8 = onView(
            allOf(
                withId(R.id.intervalViewTotalTime), withText("0:00"),
                childAtPosition(
                    allOf(
                        withId(R.id.intervalViewHeading),
                        childAtPosition(
                            IsInstanceOf.instanceOf(android.view.ViewGroup::class.java),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        textView8.check(matches(withText("0:00")))

        val appCompatImageButton = onView(
            allOf(
                withContentDescription("Navigate up"),
                childAtPosition(
                    allOf(
                        withId(R.id.action_bar),
                        childAtPosition(
                            withId(R.id.action_bar_container),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        onView(childAtPosition(withId(R.id.cardRecyclerView), 0)).perform(swipeLeft())
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
