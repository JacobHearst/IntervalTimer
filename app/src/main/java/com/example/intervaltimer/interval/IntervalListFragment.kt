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
import com.example.intervaltimer.IntervalModalFragment
import com.example.intervaltimer.R
import com.example.room.Interval
import com.example.intervaltimer.Util
import com.example.intervaltimer.interval.IntervalCardAdapter.OnEditIntervalClickedListener
import kotlinx.android.synthetic.main.fragment_interval_list.view.*

/**
 * A [Fragment] subclass representing the list of intervals in a workout.
 *
 * @property args Navigation arguments
 * @property viewModel Local reference to the [IntervalViewModel]
 */
class IntervalListFragment : Fragment(), OnEditIntervalClickedListener {
    private val args: IntervalListFragmentArgs by navArgs()
    private lateinit var listener: OnEditIntervalClickedListener
    private lateinit var viewModel: IntervalViewModel
    private lateinit var recyclerView: RecyclerView

    /**
     * Initialize data binding, recycler view
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_interval_list, container, false)
        viewModel = ViewModelProviders.of(this).get(IntervalViewModel::class.java)
        recyclerView = initRecyclerView(rootView.intervalList)

        rootView.intervalViewWorkoutName.text = args.workout.name
        rootView.intervalViewTotalTime.text =
            Util.getDurationLabel(args.workout.length)

        /**
         * Upon click, open Add Interval Modal
         */
        rootView.addInterval.setOnClickListener {
            openIntervalModal(null)
        }

        rootView.startWorkout.setOnClickListener {
            findNavController().navigate(IntervalListFragmentDirections.actionIntervalListFragmentToTimerFragment())
        }

        return rootView
    }

    /**
     * On Interval Edit button clicked, open the interval modal
     * @param interval Interval to be edited
     */
    override fun onEditClicked(interval: Interval) {
        openIntervalModal(interval)
    }

    /**
     * Initialize the [RecyclerView] with a layout manager and adapter, then populate it
     *
     * @param recyclerView Reference to the interval list [RecyclerView]
     */
    private fun initRecyclerView(recyclerView: RecyclerView): RecyclerView {
        val recyclerLayout = LinearLayoutManager(requireActivity().applicationContext)
        listener = this
        val intervalAdapter = IntervalCardAdapter(listener)

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

    /**
     * Initializes and creates the Dialog Fragment View
     * @param interval Interval to be edited
     */
    private fun openIntervalModal(interval: Interval?) {
        val dialog = IntervalModalFragment(interval)
        val bundle = Bundle()

        if (interval == null) {
            bundle.putInt("workoutId", args.workout.id as Int)
            bundle.putInt("newIndex", recyclerView.adapter?.itemCount as Int)
            // Create an instance of the dialog fragment and show it
            dialog.arguments = bundle
        }

        dialog.show(activity!!.supportFragmentManager, "IntervalModalFragment")
    }
}
