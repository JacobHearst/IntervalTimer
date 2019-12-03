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
 * Add Interval Modal View Fragment
 * #Allows the User to create/add new Intervals
 * @property binding: Asynchronous stream of data from the layout
 */
class AddWorkoutModalFragment : DialogFragment() {
    private lateinit var binding: AddWorkoutModalBinding

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
                    R.layout.add_workout_modal_title, null))
                ?.setView(binding.root)
                ?.setPositiveButton(
                    R.string.add_interval_add_dialog_text
                ) { dialog, _ ->
                    addWorkout()
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
     * Adds a new workout to the database.
     */
    private fun addWorkout() {
        val viewModel = ViewModelProviders.of(this).get(WorkoutViewModel::class.java)

        viewModel.insert(Workout(null, binding.editText.text.toString(), 0, false))
    }

    /**
     * Initializes data binding for the modal.
     */
    private fun setupDataBinding() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context!!),
            R.layout.add_workout_modal, null, false)
    }
}