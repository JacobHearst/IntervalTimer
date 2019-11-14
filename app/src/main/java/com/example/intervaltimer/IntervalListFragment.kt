package com.example.intervaltimer


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.room.Interval
import kotlinx.android.synthetic.main.fragment_interval_list.view.*

/**
 * A [Fragment] subclass representing the list of intervals in a workout.
 */
class IntervalListFragment : Fragment() {
    private val args: IntervalListFragmentArgs by navArgs()

    /**
     * Initialize data binding, recycler view
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_interval_list, container, false)
        initRecyclerView(rootView.intervalList)

        rootView.intervalViewWorkoutName.text = args.workout.name
        rootView.intervalViewTotalTime.text = Util.getDurationLabel(args.workout.length)

        rootView.addInterval.setOnClickListener {
            // TODO: Implement addition of new intervals
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

        val intervals = listOf(
            Interval(1, "6-inch lift", Interval.IntervalType.ACTIVE.value, 30, null, 1),
            Interval(2, "Small flutter kicks", Interval.IntervalType.ACTIVE.value, 30, null, 1),
            Interval(3, "Big flutter kicks", Interval.IntervalType.ACTIVE.value, 30, null, 1),
            Interval(4, "Twists", Interval.IntervalType.ACTIVE.value, 30, null, 1),
            Interval(5, "Waves", Interval.IntervalType.ACTIVE.value, 30, null, 1)
        )

        intervalAdapter.setIntervals(intervals)

//        val viewModel = ViewModelProviders.of(this).get(IntervalViewModel::class.java)
//
//        // Populate the RecyclerView
//        viewModel.getIntervalsByWorkout(args.workout.id!!).observe(this,
//            Observer<List<Interval>> { intervals ->
//                intervalAdapter.setIntervals(intervals)
//            }
//        )
    }


}
