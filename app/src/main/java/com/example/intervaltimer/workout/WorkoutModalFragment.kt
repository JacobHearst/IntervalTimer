package com.example.intervaltimer.workout

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.example.intervaltimer.R
import com.example.intervaltimer.databinding.AddWorkoutModalBinding
import com.example.room.Workout

/**
 * Workout Modal Fragment
 * #Allows the User to create/add new Intervals
 * @property binding: Asynchronous stream of data from the layout
 */
class WorkoutModalFragment(private val workout: Workout?) : DialogFragment() {
    private lateinit var binding: AddWorkoutModalBinding

    /**
     * onCreateDialog function
     * @param savedInstanceState: Current State of the App
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Setup Dialog Activity
        return activity?.let {
            binding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()),
                R.layout.add_workout_modal, null, false)

            // Build the dialog alert
            val builder: AlertDialog.Builder? = activity?.let {
                AlertDialog.Builder(it)
            }

            // Set the properties of the dialog alert
            builder
                ?.setCancelable(false)
                ?.setCustomTitle(View.inflate(context,
                    R.layout.add_workout_modal_title, null))
                ?.setView(binding.root)
                ?.setPositiveButton(
                    R.string.submit
                ) { dialog, _ ->
                    submitWorkout()
                    dialog?.dismiss()
                }
                ?.setNegativeButton(
                    R.string.add_interval_cancel_dialog_text
                ) { dialog, _ ->
                    dialog?.cancel()
                }

            builder?.show()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onStart() {
        super.onStart()
        (requireDialog() as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = workout != null
        setupDataBinding()
    }

    /**
     * Adds a new workout to the database.
     */
    private fun submitWorkout() {
        val viewModel = ViewModelProviders.of(this).get(WorkoutViewModel::class.java)
        val workoutName = binding.workoutNameEditText.text.toString()

        if (workout != null) {
            workout.name = workoutName
            viewModel.update(workout)
        } else {
            viewModel.insert(Workout(null, workoutName, 0, false))
        }
    }

    /**
     * Initializes data binding for the modal.
     */
    private fun setupDataBinding() {
        if (workout != null) {
            binding.workoutNameEditText.setText(workout.name)
        }

        binding.workoutNameEditText.addTextChangedListener {
            (requireDialog() as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = !it.isNullOrEmpty()
        }
    }
}