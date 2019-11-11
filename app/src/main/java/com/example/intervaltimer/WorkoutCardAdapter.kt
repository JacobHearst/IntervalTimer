package com.example.intervaltimer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter class for the Workout recycler view.
 *
 * @property workouts List of workouts to display
 */
class WorkoutCardAdapter(): RecyclerView.Adapter<WorkoutCardAdapter.WorkoutHolder>() {

    private var workouts: List<Workout>? = null
    private var fragment: Fragment? = null

    constructor(workouts: List<Workout>, fragment: Fragment) : this(){
        this.workouts = workouts
        this.fragment = fragment
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

            holder.workoutView.setOnClickListener {
                // TODO: There is a Room workout, and then a second Workout class. Combine the two.
                findNavController(fragment!!).navigate(LandingFragmentDirections.actionLandingFragmentToIntervalListFragment(com.example.room.Workout(0, workout.name, workout.time.toInt())));
            }
        }
        else {

            workoutName.text = "ERROR"
            workoutTime.text = "ERROR"
        }
    }
}