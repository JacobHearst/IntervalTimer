package com.example.intervaltimer


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class TimerActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun timerActivityTest() {
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
        appCompatEditText.perform(replaceText("test"), closeSoftKeyboard())

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
        appCompatEditText2.perform(replaceText("start"), closeSoftKeyboard())

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.seconds_input),
                childAtPosition(
                    allOf(
                        withId(R.id.units_input_constraint_layout),
                        childAtPosition(
                            withId(R.id.interval_modal_constraint_layout),
                            1
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatEditText3.perform(replaceText("10"), closeSoftKeyboard())

        val appCompatButton2 = onView(
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
        appCompatButton2.perform(scrollTo(), click())

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
        appCompatEditText4.perform(replaceText("middle"), closeSoftKeyboard())

        val appCompatEditText5 = onView(
            allOf(
                withId(R.id.seconds_input),
                childAtPosition(
                    allOf(
                        withId(R.id.units_input_constraint_layout),
                        childAtPosition(
                            withId(R.id.interval_modal_constraint_layout),
                            1
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatEditText5.perform(replaceText("20"), closeSoftKeyboard())

        val appCompatCheckBox = onView(
            allOf(
                withId(R.id.show_color_picker_checkbox), withText("Custom Interval Color"),
                childAtPosition(
                    allOf(
                        withId(R.id.interval_modal_constraint_layout),
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
        appCompatCheckBox.perform(click())

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

        val floatingActionButton4 = onView(
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
        floatingActionButton4.perform(click())

        val appCompatEditText6 = onView(
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
        appCompatEditText6.perform(replaceText("reps"), closeSoftKeyboard())

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

        val appCompatEditText7 = onView(
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
        appCompatEditText7.perform(replaceText("20"), closeSoftKeyboard())

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

        val floatingActionButton5 = onView(
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
        floatingActionButton5.perform(click())

        val appCompatEditText8 = onView(
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
        appCompatEditText8.perform(replaceText("end"), closeSoftKeyboard())

        val appCompatEditText9 = onView(
            allOf(
                withId(R.id.seconds_input),
                childAtPosition(
                    allOf(
                        withId(R.id.units_input_constraint_layout),
                        childAtPosition(
                            withId(R.id.interval_modal_constraint_layout),
                            1
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatEditText9.perform(replaceText("9"), closeSoftKeyboard())

        val appCompatCheckBox2 = onView(
            allOf(
                withId(R.id.show_color_picker_checkbox), withText("Custom Interval Color"),
                childAtPosition(
                    allOf(
                        withId(R.id.interval_modal_constraint_layout),
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
        appCompatCheckBox2.perform(click())

        val appCompatButton5 = onView(
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
        appCompatButton5.perform(scrollTo(), click())

        val appCompatButton6 = onView(
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
        appCompatButton6.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(700)

        val appCompatButton7 = onView(
            allOf(
                withId(R.id.endWorkoutButton), withText("End Workout"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatButton7.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(700)

        val appCompatButton8 = onView(
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
        appCompatButton8.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(700)

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
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(700)

        val appCompatButton9 = onView(
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
        appCompatButton9.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(700)

        val appCompatButton10 = onView(
            allOf(
                withId(R.id.playButton),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatButton10.perform(click())

        val appCompatButton11 = onView(
            allOf(
                withId(R.id.rewindButton),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatButton11.perform(click())

        val appCompatButton12 = onView(
            allOf(
                withId(R.id.fastForwardButton),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatButton12.perform(click())

        val appCompatButton13 = onView(
            allOf(
                withId(R.id.playButton),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatButton13.perform(click())
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
