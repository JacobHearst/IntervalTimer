package com.example.intervaltimer


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.DOWN
import androidx.recyclerview.widget.ItemTouchHelper.UP
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.room.Interval
import com.example.viewmodel.IntervalViewModel
import kotlinx.android.synthetic.main.fragment_interval_list.view.*

/**
 * A [Fragment] subclass representing the list of intervals in a workout.
 *
 * @property args Navigation arguments
 * @property itemTouchHelper Item touch helper for handling drag to reorder
 * @property viewModel Local reference to the [IntervalViewModel]
 */
class IntervalListFragment : Fragment() {
    private val args: IntervalListFragmentArgs by navArgs()
    private lateinit var viewModel: IntervalViewModel

    private val itemTouchHelper by lazy {
        val simpleItemTouchCallback =
            object : ItemTouchHelper.SimpleCallback(UP or DOWN, 0) {
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
                    val adapter = recyclerView.adapter as IntervalAdapter
                    val from = viewHolder.adapterPosition
                    val to = target.adapterPosition

                    adapter.moveItem(from, to)
                    adapter.notifyItemMoved(from, to)

                    return true
                }

                // Side to side reordering isn't supported so we don't implement this function
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

                /**
                 * User interaction has ended, persist index updates
                 *
                 * @param recyclerView The RecylerView being interacted with
                 * @param viewHolder The ViewHolder for the view being interacted with
                 */
                override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                    super.clearView(recyclerView, viewHolder)

                    val adapter = recyclerView.adapter as IntervalAdapter
                    persistIndexUpdates(adapter.getIntervals())
                }
            }

        ItemTouchHelper(simpleItemTouchCallback)
    }

    /**
     * Initialize data binding, recycler view
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_interval_list, container, false)
        viewModel = ViewModelProviders.of(this).get(IntervalViewModel::class.java)
        initRecyclerView(rootView.intervalList)

        /*
        viewModel.insertInterval(Interval(null, "Warmup", "Active", 300, null, 0, 1))
        viewModel.insertInterval(Interval(null, "Low-Intensity", "Active", 200, null, 1, 1))
        viewModel.insertInterval(Interval(null, "High-Intensity", "Active", 500, null, 2, 1))
        viewModel.insertInterval(Interval(null, "Low-Intensity", "Active", 200, null, 3, 1))
        viewModel.insertInterval(Interval(null, "Cool Down", "Active", 200, null, 4, 1))
        */

        rootView.intervalViewWorkoutName.text = args.workout.name
        rootView.intervalViewTotalTime.text = Util.getDurationLabel(args.workout.length)

        rootView.addInterval.setOnClickListener {
            // TODO: Implement addition of new intervals
        }

        rootView.startWorkout.setOnClickListener {
            // TODO: Connect to timer screen
        }

        return rootView
    }

    /**
     * Initialize the [RecyclerView] with a layout manager and adapter, then populate it
     *
     * @param recyclerView Reference to the interval list [RecyclerView]
     */
    private fun initRecyclerView(recyclerView: RecyclerView) {
        val recyclerLayout = LinearLayoutManager(requireActivity().applicationContext)
        val intervalAdapter = IntervalAdapter()

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = recyclerLayout
            adapter = intervalAdapter
        }

        // Populate the RecyclerView
        viewModel.getIntervalsByWorkout(args.workout.id!!).observe(this,
            Observer<List<Interval>> { intervals ->
                intervalAdapter.setIntervals(intervals.toMutableList())
            }
        )

        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    /**
     * Persist changes to interval order to the database
     *
     * @param intervals List of intervals with updated indices
     */
    fun persistIndexUpdates(intervals: List<Interval>) {
        intervals.forEachIndexed { index, interval ->
            interval.index = index
        }
        viewModel.updateInterval(*intervals.toTypedArray())
    }
}
