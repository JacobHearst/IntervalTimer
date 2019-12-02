package com.example.intervaltimer.workout

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.room.Workout
import com.google.android.material.snackbar.Snackbar

class WorkoutItemTouchCallback(
    private var adapter: WorkoutCardAdapter,
    private var fragment: Fragment
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    private var viewModel: WorkoutViewModel = ViewModelProviders.of(fragment).get(WorkoutViewModel::class.java)

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val workoutToDelete = adapter.getItems()[position]

        viewModel.delete(workoutToDelete)
        showUndoSnackbar(workoutToDelete)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean { return true }

    private fun showUndoSnackbar(workout: Workout) {
        val snackbar = Snackbar.make(fragment.requireView(), "Workout deleted", Snackbar.LENGTH_LONG)
        snackbar.setAction("Undo") {
            viewModel.insert(workout)
        }

        snackbar.show()
    }
}