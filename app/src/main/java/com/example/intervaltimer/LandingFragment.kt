package com.example.intervaltimer

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.room.Workout
import kotlinx.android.synthetic.main.fragment_landing.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.viewmodel.WorkoutViewModel
import kotlin.math.*

/**
 * The home screen fragment for the app. This fragment shows the user all available workouts,
 * and allows the user to navigate to other screens, including a show intervals screen, and a create
 * new workout screen.
 *
 * @property recyclerLayout Layout manager for the RecyclerView
 * @property recyclerAdapter Adapter that holds workout cards
 */
class LandingFragment : Fragment() {

    private var recyclerLayout: LinearLayoutManager? = null
    private var recyclerAdapter: WorkoutCardAdapter? = null

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

        // Needed variables for the swiping
        val MIN_SWIPE_DISTANCE = 300;  // Minimum distance to activate swipe
        val MAX_ANGLE = +30 // Max angle swipe
        val MIN_ANGLE = -30 // Min angle swipe
        var startX = 0f
        var endX = 0f
        var startY = 0f
        var endY = 0f

        rootView.cardRecyclerView?.setOnTouchListener(object: View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    startX = event.x
                    startY = event.y
                }
                if (event?.action == MotionEvent.ACTION_UP) {
                    endX = event.x
                    endY = event.y
                    var deltaX = startX - endX
                    var deltaY = startY - endY
                    var swipeDistance = abs(sqrt(deltaX.pow(2)+ deltaY.pow(2)))
                    var angle = atan2(deltaY, deltaX) * 180 / PI
                    if (swipeDistance >= MIN_SWIPE_DISTANCE && angle <= MAX_ANGLE &&
                            angle >= MIN_ANGLE) {
                        Toast.makeText(activity, "Swipe right to left", Toast.LENGTH_LONG).show()
                    }
                }
                return true
            }
        })

        val floatingActionButton =
            rootView.addWorkoutButton//rootView.findViewById<FloatingActionButton>(R.id.addWorkoutButton)

        floatingActionButton?.setOnClickListener {
            val dialog = AddWorkoutModalFragment()
            dialog.show(activity!!.supportFragmentManager, "AddWorkoutModalFragment")
        }

        val viewModel = ViewModelProviders.of(this).get(WorkoutViewModel::class.java)

        recyclerLayout = LinearLayoutManager(this.context)
        recyclerAdapter = WorkoutCardAdapter(this)

        var touchHelper = ItemTouchHelper(SwipeToDeleteHandler(recyclerAdapter!!, this))
        touchHelper.attachToRecyclerView(rootView.cardRecyclerView)

        // Populate the RecyclerView
        viewModel.getAllWorkouts().observe(this,
            Observer<List<Workout>> { workouts ->
                recyclerAdapter?.setWorkouts(workouts.toMutableList())
            }
        )

        rootView.findViewById<RecyclerView>(R.id.cardRecyclerView).apply {
            layoutManager = recyclerLayout
            adapter = recyclerAdapter
        }

        return rootView
    }


}
