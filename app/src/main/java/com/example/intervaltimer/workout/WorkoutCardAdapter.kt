package com.example.intervaltimer.workout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.intervaltimer.R
import com.example.intervaltimer.Util
import com.example.room.Workout
import kotlinx.android.synthetic.main.workout_card.view.*

/**
 * Adapter class for the Workout recycler view.
 *
 * @property workouts List of workouts to display
 * @property fragment The fragment that this adapter belongs to
 * @property viewModel View Model for inserting into the database
 */
class WorkoutCardAdapter(): RecyclerView.Adapter<WorkoutCardAdapter.WorkoutHolder>() {

    private var workouts: MutableList<Workout>? = null
    private var fragment: Fragment? = null
    private var viewModel: WorkoutViewModel? = null

    constructor(fragment: Fragment) : this() {
        this.fragment = fragment
        this.viewModel = ViewModelProviders.of(fragment).get(WorkoutViewModel::class.java)
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
     * @param items List to display
     */
    fun setItems(items: List<Workout>) {
        workouts = items.toMutableList()

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

        val workoutName = holder.workoutView.findViewById<TextView>(R.id.workoutNameText)
        val workoutTime = holder.workoutView.findViewById<TextView>(R.id.workoutTimeText)

        // If there is data to display
        if(workouts != null) {

            // Get a workout from the list. If it doesn't exist, create a blank workout
            val workout = workouts?.get(position) ?: Workout(0,"", 0, false)

            workoutName.text = workout.name
            workoutTime.text = Util.getDurationLabel(workout.length)

            // When clicked, navigate to the interval view screen and pass the workout
            holder.workoutView.setOnClickListener {
                findNavController(fragment!!).navigate(WorkoutListFragmentDirections.actionLandingFragmentToIntervalListFragment(workout))
            }

            val favoriteButton = holder.workoutView.favoriteButton

            favoriteButton.setOnClickListener {
                // Flip value
                workout.isFavorite = !workout.isFavorite

                viewModel?.update(workout)

                if(workout.isFavorite) {
                    favoriteButton.background = fragment?.context?.getDrawable(R.drawable.ic_star_filled)
                } else {
                    favoriteButton.background = fragment?.context?.getDrawable(R.drawable.ic_star_empty)
                }
            }

            if(workout.isFavorite) {
                favoriteButton.background = fragment?.context?.getDrawable(R.drawable.ic_star_filled)
            } else {
                favoriteButton.background = fragment?.context?.getDrawable(R.drawable.ic_star_empty)
            }
        }
        else {

            workoutName.text = "ERROR"
            workoutTime.text = "ERROR"
        }
    }

    fun getItems() : List<Workout> = workouts!!.toList()

    fun deleteItem(position: Int) {
        workouts?.removeAt(position)
    }
}