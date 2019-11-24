package com.example.intervaltimer

import android.graphics.Canvas
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.room.Workout
import com.example.viewmodel.WorkoutViewModel
import com.google.android.material.snackbar.Snackbar

class SwipeToDeleteHandler : ItemTouchHelper.SimpleCallback {

    private var adapter: WorkoutCardAdapter
    private var viewModel: WorkoutViewModel
    private var fragment: Fragment

    constructor (adapter: WorkoutCardAdapter, fragment: Fragment) : super(ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT) {
        this.adapter = adapter
        this.fragment = fragment
        this.viewModel = ViewModelProviders.of(fragment).get(WorkoutViewModel::class.java)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val workoutToDelete = adapter.getItem(position)

        viewModel.deleteWorkout(workoutToDelete);
        showUndoSnackbar(workoutToDelete)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    private fun showUndoSnackbar(workout: Workout) {
        var snackbar = Snackbar.make(fragment.view!!, "Workout deleted! Undo?", Snackbar.LENGTH_LONG)
        snackbar.setAction("Undo", View.OnClickListener {
            viewModel.insertWorkout(workout)
        })

        snackbar.show()
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

}