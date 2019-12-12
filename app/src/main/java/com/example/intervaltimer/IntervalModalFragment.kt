package com.example.intervaltimer

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.intervaltimer.databinding.FragmentIntervalModalBinding
import com.example.intervaltimer.interval.IntervalViewModel
import com.example.intervaltimer.workout.WorkoutViewModel
import com.example.room.Interval
import com.example.room.Workout
import com.skydoves.colorpickerview.listeners.ColorListener
import kotlinx.android.synthetic.main.fragment_interval_list.*
import kotlinx.android.synthetic.main.fragment_interval_modal.view.*
import java.util.*

/**
 * Add Interval Modal View Fragment
 * #Allows the User to create/add new Intervals
 * @property binding: Asynchronous stream of data from the layout
 */
class IntervalModalFragment(interval: Interval?) : DialogFragment() {
    private lateinit var binding: FragmentIntervalModalBinding
    private lateinit var dialog: AlertDialog
    private var mInterval: Interval? = interval
    private lateinit var fragment: Fragment

    /**
     * onCreateDialog function
     * @param savedInstanceState: Current State of the App
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Setup Dialog Activity
        return activity?.let {
            val isIntervalEdit: Boolean = mInterval != null
            setupDataBinding(isIntervalEdit)
            // Build the dialog alert
            val builder: AlertDialog.Builder? = activity?.let {
                AlertDialog.Builder(it)
            }

            // Set the properties of the dialog alert
            builder
                ?.setCancelable(false)
                ?.setCustomTitle(View.inflate(
                    context,
                    if (!isIntervalEdit)
                        R.layout.add_interval_timer_modal_title else R.layout.edit_interval_timer_modal_title,
                    null))
                ?.setView(binding.root)
                ?.setPositiveButton(R.string.add_interval_add_dialog_text
                ) { _, _ ->
                }
                ?.setNegativeButton(R.string.add_interval_cancel_dialog_text
                ) { dialog, _ ->
                    dialog?.cancel()
                }

            builder?.show()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    /**
     * Override the onStart to setup the add button behavior
     */
    override fun onStart() {
        super.onStart()
        dialog = getDialog() as AlertDialog

        val positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            // Values being validated
            val intervalName: String = binding.intervalNameInput.text.toString()
            val intervalReps: Int = if (binding.repsInput.text.isNotEmpty())
                Integer.parseInt(binding.repsInput.text!!.toString()) else 0
            val intervalMinutes: Int = if (binding.minutesInput.text.isNotEmpty()) Integer.parseInt(binding.minutesInput.text.toString()) else 0
            val intervalSeconds: Int = if (binding.secondsInput.text.isNotEmpty()) Integer.parseInt(binding.secondsInput.text.toString()) else 0

            val intervalTime: Int = intervalMinutes * 60 + intervalSeconds

            // Display message for each validation failure, or submit the interval
            if (intervalName.isBlank()) {
                Toast.makeText(context, R.string.interval_name_empty_error, Toast.LENGTH_SHORT).show()
            } else if (binding.repsRadioButton.isChecked && intervalReps == 0) {
                Toast.makeText(context, R.string.interval_reps_empty_error, Toast.LENGTH_SHORT).show()
            } else if (binding.timerRadioButton.isChecked && intervalTime == 0) {
                Toast.makeText(context, R.string.interval_timer_empty_error, Toast.LENGTH_SHORT).show()
            } else {
                submitInterval(mInterval != null)
                dismiss()
            }
        }
    }

    /**
     *
     */
    fun addFragmentReference(fragment: Fragment) {
        this.fragment = fragment
    }

    /**
     * addInterval: Add Interval to Database
     */
    private fun submitInterval(isUpdate: Boolean) {
        // Get Inputs
        val intervalName: String = binding.intervalNameInput.text.toString()
        var intervalReps: Int = if (binding.repsInput.text.isNotEmpty())
            Integer.parseInt(binding.repsInput.text!!.toString()) else 0

        val intervalMinutes: Int = if (binding.minutesInput.text.isNotEmpty()) Integer.parseInt(binding.minutesInput.text.toString()) else 0
        val intervalSeconds: Int = if (binding.secondsInput.text.isNotEmpty()) Integer.parseInt(binding.secondsInput.text.toString()) else 0
        var intervalTime: Int = intervalMinutes * 60 + intervalSeconds

        val intervalColor: String = binding.intervalColorPicker.colorEnvelope.hexCode

        val workout = arguments!!.getSerializable("workout") as Workout
        val intervalIndex = arguments!!.getInt("newIndex")
        val workoutId = workout.id!!

        // Get Input Types
        val intervalWorkoutType: String = (
                getString(
                    if (binding.activeRadioButton.isChecked)
                        R.string.add_interval_active_text
                    else
                        R.string.add_interval_resting_text
                )).toUpperCase(Locale.ENGLISH)

        // Encode interval with reps
        if(intervalReps > 0) {
            intervalTime = 0
        }
        // Encode timed interval
        else {
            intervalReps = 0
        }

        // Build Interval to Add (If) or Edit (Else)
        val intervalToSubmit = if (!isUpdate)
            Interval(
                null,
                intervalName,
                intervalWorkoutType,
                if(intervalReps == 0) intervalTime else 0, // Insert time only if interval reps is 0
                if(intervalReps == 0) null else intervalReps,
                intervalColor,
                intervalIndex,
                workoutId)
        else
            Interval(
                mInterval?.id,
                intervalName,
                intervalWorkoutType,
                if(intervalReps == 0) intervalTime else 0, // Insert time only if interval reps is 0
                if(intervalReps == 0) null else intervalReps,
                intervalColor,
                mInterval!!.index,
                mInterval!!.workoutId)
        val viewModel = ViewModelProviders.of(this).get(IntervalViewModel::class.java)
        val workoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel::class.java)

        // Remove the old time from the workout and add the new one, but only remove the old time if the action is an edit
        if(isUpdate && mInterval != null && mInterval?.time != null) {
            workout.length -= mInterval?.time!!
        }

        workout.length += intervalTime

        // Update the workout
        workoutViewModel.update(workout)

        if (isUpdate) viewModel.update(intervalToSubmit) else viewModel.insert(intervalToSubmit)

        fragment.intervalViewTotalTime.text = Util.getDurationLabel(workout.length)
    }

    /**
     * setupDataBinding: Initialize and organize data-binding
     */
    private fun setupDataBinding(isUpdate: Boolean) {

        // Setup data-binding
        binding = DataBindingUtil.inflate(LayoutInflater.from(context!!), R.layout.fragment_interval_modal, null, false)

        // Determine when the app allows the specification of interval units (i.e. Timer, Reps)
        binding.activeRadioButton.setOnCheckedChangeListener { compoundButton, _ ->
            binding.colorPickerBox.setBackgroundColor(Color.rgb(222, 215, 18))
            binding.repsRadioButton.isEnabled = compoundButton.isChecked
            // Default Option
            binding.timerRadioButton.isChecked = true
        }

        binding.restingRadioButton.setOnCheckedChangeListener { compoundButton, _ ->
            binding.colorPickerBox.setBackgroundColor(Color.rgb(20, 186, 40))
            binding.colorPickerBox.setBackgroundColor(Color.GREEN)
            binding.repsRadioButton.isEnabled = !compoundButton.isChecked
            // Default Option
            binding.timerRadioButton.isChecked = true
        }

        binding.timerRadioButton.setOnCheckedChangeListener { compoundButton, _ ->
            binding.minutesInput.isVisible = compoundButton.isChecked
            binding.minutesText.isVisible = compoundButton.isChecked
            binding.secondsInput.isVisible = compoundButton.isChecked
            binding.secondsText.isVisible = compoundButton.isChecked
            binding.repsInput.isVisible = !compoundButton.isChecked
        }

        binding.repsRadioButton.setOnCheckedChangeListener { compoundButton, _ ->
            binding.repsInput.isVisible = compoundButton.isChecked
            binding.minutesInput.isVisible = !compoundButton.isChecked
            binding.minutesText.isVisible = !compoundButton.isChecked
            binding.secondsInput.isVisible = !compoundButton.isChecked
            binding.secondsText.isVisible = !compoundButton.isChecked
        }

        // Determine if the app should show the color-picker
        binding.showColorPickerCheckbox.setOnCheckedChangeListener { compoundButton, _ ->
            binding.colorPickerBox.isVisible = compoundButton.isChecked
            binding.intervalColorPicker.isVisible = compoundButton.isChecked
            if (!compoundButton.isChecked) {
                val color: Int = if (binding.activeRadioButton.isChecked) Color.YELLOW else Color.GREEN
                binding.colorPickerBox.setBackgroundColor(color)
            }
        }
        // Set Listener to display currently selected color
        binding.intervalColorPicker.setColorListener(ColorListener { color, _ ->
            // Pass color-picker data to adjacent block to display which color is selected
            binding.colorPickerBox.setBackgroundColor(color)
        })

        // Restore existing values to the modal
        if (isUpdate) {
            if (mInterval?.type == getString(R.string.add_interval_active_text))
                binding.activeRadioButton.isChecked = true
            else binding.restingRadioButton.isChecked = true

            if (mInterval?.reps != null) {
                binding.repsRadioButton.isChecked = true
                binding.repsInput.setText(mInterval?.reps.toString())
            }
            else {
                binding.timerRadioButton.isChecked = true
                binding.minutesInput.setText((mInterval?.time?.div(60)).toString())
                binding.secondsInput.setText((mInterval?.time?.rem(60)).toString())
            }

            /*
            binding.minutesInput.setOnKeyListener { textView, i, keyEvent ->
                if (textView.minutes_input.text.toString().isNotBlank()) {
                    val inputValue = Integer.parseInt(textView.minutes_input.text.toString())
                    val inputValid = inputValue < 60
                    if (!inputValid) {
                        textView?.minutes_input?.setText("59")
                    }
                    false
                } else {
                    true
                }
            }

            binding.secondsInput.setOnKeyListener { textView, i, keyEvent ->
                if (textView.seconds_input.text.toString().isNotBlank()) {
                    val inputValue = Integer.parseInt(textView.seconds_input.text.toString())
                    val inputValid = inputValue < 60
                    if (!inputValid) {
                        textView?.seconds_input?.setText("59")
                        binding.minutesInput?.setText((Integer.parseInt(binding.minutesInput.text.toString()) + inputValue / 60).toString())
                    }
                    false
                } else {
                    true
                }
            }
            */

            binding.intervalNameInput.setText(mInterval?.name)
            binding.colorPickerBox.setBackgroundColor(mInterval!!.color.hashCode())
        }
    }
}
