package com.example.subshop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.intervaltimer.R
import com.example.intervaltimer.Workout
import java.util.*

/**
 * Adapter class for the Workou recycler view.
 *
 * @property workouts List of workouts to display
 */
class WorkoutCardAdapter(): RecyclerView.Adapter<WorkoutCardAdapter.WorkoutHolder>() {

    private var workouts: List<Workout>? = null

    constructor(workouts: List<Workout>) : this(){
        this.workouts = workouts
    }

    /**
     * Holds a simple view to display information about the workout
     *
     * @param workoutView View to display
     */
    class WorkoutHolder(val workoutView: View): RecyclerView.ViewHolder(workoutView)

    /**
     * Sets the workouts to display
     *
     * @param newWorkoutList List to display
     */
    fun setWorkouts(newWorkoutList: List<Workout>) {
        workouts = newWorkoutList

        notifyDataSetChanged()
    }

    /**
     * Gets the number of items in the list to display
     *
     * @return Number of items
     */
    override fun getItemCount(): Int {
        return workouts?.size ?: 0
    }

    /**
     * Creates simple view for the workout information
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutHolder {
        val view = (LayoutInflater.from(parent.context).inflate(R.layout.workout_card, parent, false))

        return WorkoutHolder(view)
    }

    /**
     * Modifies the data of the inflated view, adding information about the workout
     *
     * @param holder Class to modify
     * @param position Position in list to modify
     */
    override fun onBindViewHolder(holder: WorkoutHolder, position: Int) {

        // TODO: Databinding
        val workoutName = holder.workoutView.findViewById<TextView>(R.id.workoutNameText)
        val workoutTime = holder.workoutView.findViewById<TextView>(R.id.workoutTimeText)

        // If there is data to display
        if(workouts != null) {

            // Get a workout from the list. If it doesn't exist, create a blank workout
            val workout = workouts?.get(position) ?: Workout(0,"", 0.0, false);

            workoutName.text = workout.name
            workoutTime.text = workout.time.toString() + " minutes"
        }
        else {

            workoutName.text = "ERROR"
            workoutTime.text = "ERROR"
        }
    }
}