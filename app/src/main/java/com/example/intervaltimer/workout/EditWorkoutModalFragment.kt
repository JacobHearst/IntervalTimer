package com.example.intervaltimer.workout

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.example.intervaltimer.R
import com.example.intervaltimer.databinding.AddWorkoutModalBinding
import com.example.room.Workout

/**
 * Edit Interval Modal View Fragment
 * #Allows the User to edit workouts
 * @property binding: Asynchronous stream of data from the layout
 * @property workoutToEdit: Workout that will be edited
 */
class EditWorkoutModalFragment : DialogFragment () {
    private lateinit var binding: AddWorkoutModalBinding
    private var workoutToEdit: Workout? = null

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
                    R.layout.edit_workout_modal_title, null))
                ?.setView(binding.root)
                ?.setPositiveButton(
                    R.string.edit_workout_positive_button_text
                ) { dialog, _ ->
                    editWorkout()
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

    /**
     * Set the workout to be edited
     *
     * @param workout Workout to be edited
     */
    public fun setWorkout(workout: Workout) {
        workoutToEdit = workout
    }

    /**
     * Adds a new workout to the database.
     */
    private fun editWorkout() {
        val viewModel = ViewModelProviders.of(this).get(WorkoutViewModel::class.java)

        if(workoutToEdit != null) {
            workoutToEdit?.name = binding.editText.text.toString()

            viewModel.update(workoutToEdit!!)
        }
    }

    /**
     * Initializes data binding for the modal.
     */
    private fun setupDataBinding() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context!!),
            R.layout.add_workout_modal, null, false)
    }
}