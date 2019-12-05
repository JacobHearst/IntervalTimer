package com.example.intervaltimer.interval

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.intervaltimer.R
import com.example.intervaltimer.Util
import com.example.intervaltimer.databinding.FragmentIntervalModalBinding
import com.example.intervaltimer.workout.WorkoutViewModel
import com.example.room.Interval
import com.example.room.Workout
import com.skydoves.colorpickerview.listeners.ColorListener
import kotlinx.android.synthetic.main.fragment_interval_list.*
import java.util.*

/**
 * Add Interval Modal View Fragment
 * #Allows the User to create/add new Intervals
 * @property binding: Asynchronous stream of data from the layout
 */
class AddIntervalModalFragment : DialogFragment() {
    private lateinit var binding: FragmentIntervalModalBinding
    private lateinit var fragment: Fragment

    // TODO: Implement input validation
//    private var nameInputValid: Boolean = false
//    private var unitsInputValid: Boolean = false

    /**
     * onCreateDialog function
     * @param savedInstanceState: Current State of the App
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Setup Dialog Activity
        return activity?.let {
            setupDataBinding()
            // Build the dialog alert
            val builder: AlertDialog.Builder? = activity?.let {
                AlertDialog.Builder(it)
            }

            // Set the properties of the dialog alert
            builder
                ?.setCancelable(false)
                ?.setCustomTitle(View.inflate(context,
                    R.layout.add_interval_timer_modal_title, null))
                ?.setView(binding.root)
                ?.setPositiveButton(
                    R.string.add_interval_add_dialog_text
                ) { dialog, _ ->
                    addInterval()
                    dialog?.dismiss()
                }
                ?.setNegativeButton(
                    R.string.add_interval_cancel_dialog_text
                ) { dialog, _ ->
                    dialog?.cancel()
                }

            // TODO: Implement input validation for Dialog Fragment
//            binding.intervalNameInput.addTextChangedListener { text ->
//                nameInputValid = text!!.isNotEmpty()
//                addButton?.isEnabled = nameInputValid && unitsInputValid
//            }
//            binding.repsTimerInput.addTextChangedListener { text ->
//                unitsInputValid = Integer.parseInt(text.toString()) > 0 && text!!.isNotEmpty()
//                addButton?.isEnabled = unitsInputValid && nameInputValid
//            }

            builder?.show()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun addFragmentReference(fragment: Fragment) {
        this.fragment = fragment
    }

    /**
     * addInterval: Add Interval to Database
     */
    private fun addInterval() {
        // Get Inputs
        val intervalName: String = binding.intervalNameInput.text.toString()
        var intervalUnits: Int = Integer.parseInt(binding.repsTimerInput.text.toString())
        val intervalColor: String = binding.intervalColorPicker.colorEnvelope.hexCode

        // Get Input Types
        val intervalWorkoutType: String = binding.workoutTypeToggleButton.text.toString().toUpperCase(Locale.ENGLISH)
        val intervalUnitType: String = binding.workoutUnitsToggle.text.toString().toUpperCase(Locale.ENGLISH)
        val intervalTimerUnitType: String = binding.timerUnitToggleButton.text.toString()

        val timerText: String = getString(R.string.add_interval_timer_toggle_text).toUpperCase(Locale.ENGLISH)
        val repsText: String = getString(R.string.add_interval_reps_toggle_text).toUpperCase(Locale.ENGLISH)
        val minutesText: String = getString(R.string.interval_unit_minutes)

        val workout = arguments!!.getSerializable("workout") as Workout
        val intervalIndex = arguments!!.getInt("newIndex")
        val workoutId = workout.id!!

        // If Input Type is Timer, check if we need to convert Minutes to Seconds
        if (intervalUnitType == timerText)
            if (intervalTimerUnitType == minutesText)
                intervalUnits *= 60

        // Build Interval to Add
        val intervalToAdd = Interval(
            null,
            intervalName,
            intervalWorkoutType,
            if (intervalUnitType == timerText) intervalUnits else null,
            if (intervalUnitType == repsText) intervalUnits else null,
            intervalColor,
            intervalIndex,
            workoutId
            )
        val viewModel = ViewModelProviders.of(this).get(IntervalViewModel::class.java)
        val workoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel::class.java)

        workout.length += intervalUnits
        workoutViewModel.update(workout)

        viewModel.insert(intervalToAdd)

        fragment.intervalViewTotalTime.text = Util.getDurationLabel(workout.length)
    }

    /**
     * setupDataBinding: Initialize and organize data-binding
     */
    private fun setupDataBinding() {
        // Setup data-binding
        binding = DataBindingUtil.inflate(LayoutInflater.from(context!!),
            R.layout.add_interval_timer_modal, null, false)
        // Determine when the app allows the specification of interval units (i.e. Timer, Reps)
        binding.workoutTypeToggleButton.setOnCheckedChangeListener { compoundButton, _ ->
            // If Resting is selected, set workoutUnits to Timer and disable toggle button
            val color: Int = if (!compoundButton.isChecked) Color.YELLOW else Color.GREEN
            binding.workoutUnitsToggle.isChecked = !compoundButton.isChecked
            binding.workoutUnitsToggle.isEnabled = compoundButton.isChecked
            binding.colorPickerBox.setBackgroundColor(color)
        }
        // Determine if the app should show the timer units
        binding.workoutUnitsToggle.setOnCheckedChangeListener { compoundButton, _ ->
            binding.timerUnitToggleButton.isVisible = compoundButton.isChecked
        }
        // Determine if the app should show the color-picker
        binding.showColorPickerCheckbox.setOnCheckedChangeListener { compoundButton, _ ->
            binding.colorPickerBox.isVisible = compoundButton.isChecked
            binding.intervalColorPicker.isVisible = compoundButton.isChecked
            if (!compoundButton.isChecked) {
                val color: Int = if (!binding.workoutTypeToggleButton.isChecked) Color.YELLOW else Color.GREEN
                binding.colorPickerBox.setBackgroundColor(color)
            }
        }
        // Set Listener to display currently selected color
        binding.intervalColorPicker.setColorListener(ColorListener { color, _ ->
            // Pass color-picker data to adjacent block to display which color is selected
            binding.colorPickerBox.setBackgroundColor(color)
        })
    }
}
