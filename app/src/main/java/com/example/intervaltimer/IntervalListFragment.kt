package com.example.intervaltimer


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.intervaltimer.databinding.FragmentIntervalListBinding
import com.example.room.Interval
import com.example.room.Workout
import com.example.viewmodel.IntervalViewModel

/**
 * A [Fragment] subclass representing the list of intervals in a workout.
 */
class IntervalListFragment : Fragment() {
    // TODO: Replace this with input from navigation args
    private val workoutData = Workout(1, "Workout 1", 300)

    /**
     * Initialize data binding, recycler view
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentIntervalListBinding>(inflater, R.layout.fragment_interval_list, container, false)
        initRecyclerView(binding.intervalList)

        this.activity?.actionBar?.title = workoutData.workoutName

        binding.addInterval.setOnClickListener {
            // TODO: Implement addition of new intervals
        }

        return binding.root
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

        val viewModel = ViewModelProviders.of(this).get(IntervalViewModel::class.java)

        viewModel.getIntervalsByWorkout(workoutData.workoutId!!).observe(this,
            Observer<List<Interval>> { intervals ->
                intervalAdapter.setIntervals(intervals)
            })
    }


}
