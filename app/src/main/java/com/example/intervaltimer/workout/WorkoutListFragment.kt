package com.example.intervaltimer.workout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.intervaltimer.R
import com.example.room.Workout
import kotlinx.android.synthetic.main.fragment_landing.view.*

/**
 * The home screen fragment for the app. This fragment shows the user all available workouts,
 * and allows the user to navigate to other screens, including a show intervals screen, and a create
 * new workout screen.
 *
 */
class WorkoutListFragment : Fragment() {
    /**
     * Called when the fragment is first initialized.
     *
     * @param inflater Inflates any components, including the view of this fragment
     * @param container Base layout class for the fragment
     * @param savedInstanceState Cached state data
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_landing, container, false)

        val floatingActionButton = rootView.addWorkoutButton//rootView.findViewById<FloatingActionButton>(R.id.addWorkoutButton)

        floatingActionButton?.setOnClickListener {
            val dialog = WorkoutModalFragment(null)
            dialog.show(activity!!.supportFragmentManager, "AddWorkoutModalFragment")
        }

        initRecylerView(rootView.cardRecyclerView)

        return rootView
    }

    /**
     * Initialize the RecyclerView
     *
     * @param recylerView RecylerView to initialize
     */
    private fun initRecylerView(recylerView: RecyclerView) {
        val recyclerLayout = LinearLayoutManager(this.context)
        val recyclerAdapter = WorkoutCardAdapter(this)

        val viewModel = ViewModelProviders.of(this).get(WorkoutViewModel::class.java)

        val touchHelper = ItemTouchHelper(
            WorkoutItemTouchCallback(
                recyclerAdapter,
                this
            )
        )
        touchHelper.attachToRecyclerView(recylerView)

        // Populate the RecyclerView
        viewModel.getAllWorkouts().observe(this,
            Observer<List<Workout>> { workouts ->
                recyclerAdapter.setItems(workouts.toMutableList())
            }
        )

        recylerView.apply {
            layoutManager = recyclerLayout
            adapter = recyclerAdapter
        }
    }

}
