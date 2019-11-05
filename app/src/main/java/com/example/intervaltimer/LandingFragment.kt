package com.example.intervaltimer


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.subshop.WorkoutCardAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_landing.view.*

/**
 * A simple [Fragment] subclass.
 */
class LandingFragment : Fragment() {

    var recyclerLayout: LinearLayoutManager? = null
    var recyclerAdapter: WorkoutCardAdapter? = null

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

        val workouts = listOf<Workout>(Workout("Workout 1", 0.4, false),
                                       Workout("Workout 2", 1.1, true))

        recyclerLayout = LinearLayoutManager(this.context)
        recyclerAdapter = WorkoutCardAdapter(workouts)

        rootView.findViewById<RecyclerView>(R.id.cardRecyclerView).apply {
            layoutManager = recyclerLayout
            adapter = recyclerAdapter
        }

        return rootView
    }

}
