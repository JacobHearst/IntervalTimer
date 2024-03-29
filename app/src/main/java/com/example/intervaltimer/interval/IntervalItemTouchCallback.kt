package com.example.intervaltimer.interval

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import com.example.intervaltimer.Util
import com.example.intervaltimer.workout.WorkoutViewModel
import com.example.room.Interval
import com.example.room.Workout
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_interval_list.*

class IntervalItemTouchCallback(
    private var cardAdapter: IntervalCardAdapter,
    private var fragment: Fragment,
    private var workout: Workout
) : ItemTouchHelper.SimpleCallback(UP or DOWN, LEFT) {
    private var viewModel = ViewModelProviders.of(fragment).get(IntervalViewModel::class.java)
    private var workoutViewModel = ViewModelProviders.of(fragment).get(WorkoutViewModel::class.java)
    private var wasSwiped = false

    /**
     * Handle dragging and reordering
     *
     * @param recyclerView The RecylerView being interacted with
     * @param viewHolder The ViewHolder for the view being interacted with
     * @param target The target ViewHolder
     */
    override fun onMove(recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean {
        val adapter = recyclerView.adapter as IntervalCardAdapter
        val from = viewHolder.adapterPosition
        val to = target.adapterPosition

        adapter.moveItem(from, to)
        adapter.notifyItemMoved(from, to)

        return true
    }

    /**
     * Delete interval on swipe
     *
     * @param viewHolder ViewHolder for the swiped interval
     * @param direction Direction of the swipe
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val intervalToDelete = cardAdapter.getItems()[position]

        processTimeChanges(intervalToDelete, false)

        viewModel.delete(intervalToDelete)
        showUndoSnackbar(intervalToDelete)
        wasSwiped = true
    }

    /**
     * User interaction has ended, persist index updates
     *
     * @param recyclerView The RecylerView being interacted with
     * @param viewHolder The ViewHolder for the view being interacted with
     */
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        if (!wasSwiped) {
            persistIndexUpdates()
        }
        wasSwiped = false
    }

    /**
     * Display a snackbar that allows the user to undo the delete
     *
     * @param interval The deleted interval
     */
    private fun showUndoSnackbar(interval: Interval) {
        val snackbar = Snackbar.make(fragment.requireView(), "Interval deleted", Snackbar.LENGTH_LONG)
        snackbar.setAction("Undo") {
            viewModel.insert(interval)
            cardAdapter.insertItem(interval.index, interval)
            persistIndexUpdates()

            processTimeChanges(interval, true)
        }

        snackbar.show()
    }

    /**
     * Persist changes to interval order to the database
     */
    private fun persistIndexUpdates() {
        val intervals = cardAdapter.getItems()
        intervals.forEachIndexed { index, interval ->
            interval.index = index
            viewModel.update(interval)
        }
    }

    private fun processTimeChanges(interval: Interval, isBeingAdded: Boolean) {

        if(interval.time != null) {
            // Update the time in the database
            if(isBeingAdded) {
                workout.length += interval.time!!
            } else {
                workout.length -= interval.time!!
            }
        }

        workoutViewModel.update(workout)

        // Update UI
        fragment.intervalViewTotalTime.text = Util.getDurationLabel(workout.length)
    }
}