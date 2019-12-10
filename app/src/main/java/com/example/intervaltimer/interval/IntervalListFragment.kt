package com.example.intervaltimer.interval


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
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
import com.example.intervaltimer.Util
import com.example.intervaltimer.interval.IntervalCardAdapter.OnEditIntervalClickedListener
import com.example.room.Interval
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
    private lateinit var zeroStateLabel: TextView

    /**
     * Initialize data binding, recycler view
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_interval_list, container, false)
        viewModel = ViewModelProviders.of(this).get(IntervalViewModel::class.java)

        zeroStateLabel = rootView.intervalZeroStateLabel
        recyclerView = initRecyclerView(rootView.intervalList)

        rootView.intervalViewWorkoutName.text = args.workout.name
        rootView.intervalViewTotalTime.text = Util.getDurationLabel(args.workout.length)

        /**
         * Upon click, open Add Interval Modal
         */
        rootView.addInterval.setOnClickListener {
            openIntervalModal(null)
        }

        rootView.startWorkout.setOnClickListener {

            if(recyclerView.adapter!!.itemCount > 0) {
                findNavController().navigate(IntervalListFragmentDirections.actionIntervalListFragmentToTimerFragment())
            } else {
                Toast.makeText(this.context, "This workout is empty!", Toast.LENGTH_LONG).show()
            }
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
                intervalAdapter.setItems(intervals.toMutableList())
                zeroStateLabel.visibility = if (intervals.isEmpty()) VISIBLE else GONE
                recyclerView.visibility = if (intervals.isEmpty()) GONE else VISIBLE
            }
        )

        ItemTouchHelper(
            IntervalItemTouchCallback(intervalAdapter, this, args.workout)
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

        dialog.addFragmentReference(this)

        // If the interval is a new interval
        if (interval == null) {
            bundle.putInt("newIndex", recyclerView.adapter?.itemCount as Int)
        }

        // Pass a reference to the workout for UI and database updates
        bundle.putSerializable("workout", args.workout)
        dialog.arguments = bundle

        dialog.show(activity!!.supportFragmentManager, "IntervalModalFragment")
    }
}
