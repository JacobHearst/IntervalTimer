package com.example.intervaltimer.interval


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.intervaltimer.R
import com.example.room.Interval
import com.example.intervaltimer.Util
import kotlinx.android.synthetic.main.fragment_interval_list.view.*

/**
 * A [Fragment] subclass representing the list of intervals in a workout.
 *
 * @property args Navigation arguments
 * @property viewModel Local reference to the [IntervalViewModel]
 */
class IntervalListFragment : Fragment() {
    private val args: IntervalListFragmentArgs by navArgs()
    private lateinit var viewModel: IntervalViewModel

    /**
     * Initialize data binding, recycler view
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_interval_list, container, false)
        viewModel = ViewModelProviders.of(this).get(IntervalViewModel::class.java)
        val recyclerView = initRecyclerView(rootView.intervalList)

        rootView.intervalViewWorkoutName.text = args.workout.name
        rootView.intervalViewTotalTime.text =
            Util.getDurationLabel(args.workout.length)

        /**
         * Upon click, open Add Interval Modal
         */
        rootView.addInterval.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("workoutId", args.workout.id as Int)
            bundle.putInt("newIndex", recyclerView.adapter?.itemCount as Int)
            // Create an instance of the dialog fragment and show it
            val dialog = AddIntervalModalFragment()
            dialog.arguments = bundle
            dialog.show(activity!!.supportFragmentManager, "AddIntervalModalFragment")
        }

        rootView.startWorkout.setOnClickListener {
            findNavController().navigate(IntervalListFragmentDirections.actionIntervalListFragmentToTimerFragment())
        }

        return rootView
    }

    /**
     * Initialize the [RecyclerView] with a layout manager and adapter, then populate it
     *
     * @param recyclerView Reference to the interval list [RecyclerView]
     */
    private fun initRecyclerView(recyclerView: RecyclerView): RecyclerView {
        val recyclerLayout = LinearLayoutManager(requireActivity().applicationContext)
        val intervalAdapter =
            IntervalCardAdapter()

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = recyclerLayout
            adapter = intervalAdapter
        }

        // Populate the RecyclerView
        viewModel.getIntervalsByWorkout(args.workout.id!!).observe(this,
            Observer<List<Interval>> { intervals ->
                Log.d("INTERVAL_LIST_FRAGMENT", intervals.toString())
                intervalAdapter.setItems(intervals.toMutableList())
            }
        )

        ItemTouchHelper(
            IntervalItemTouchCallback(intervalAdapter, this)
        ).attachToRecyclerView(recyclerView)

        return recyclerView
    }
}
