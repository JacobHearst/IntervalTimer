package com.example.intervaltimer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.intervaltimer.WorkoutCardAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_landing.view.*

/**
 * The home screen fragment for the app. This fragment shows the user all available workouts,
 * and allows the user to navigate to other screens, including a show intervals screen, and a create
 * new workout screen.
 *
 * @property recyclerLayout Layout manager for the RecyclerView
 * @property recyclerAdapter Adapter that holds workout cards
 */
class LandingFragment : Fragment() {

    var recyclerLayout: LinearLayoutManager? = null
    var recyclerAdapter: WorkoutCardAdapter? = null

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

        val mFab = rootView.addWorkoutButton//rootView.findViewById<FloatingActionButton>(R.id.addWorkoutButton)
        mFab?.setOnClickListener {
            Toast.makeText(this.context, "TODO: Navigate to new workout screen", Toast.LENGTH_LONG).show()
        }

        val workouts = listOf<Workout>(Workout(1,"Workout 1", 0.4, false),
                                       Workout(2,"Workout 2", 1.1, true),
                                       Workout(3, "Back Workout", 16.8, true) )

        recyclerLayout = LinearLayoutManager(this.context)
        recyclerAdapter = WorkoutCardAdapter(workouts)

        rootView.findViewById<RecyclerView>(R.id.cardRecyclerView).apply {
            layoutManager = recyclerLayout
            adapter = recyclerAdapter
        }

        return rootView
    }

}
