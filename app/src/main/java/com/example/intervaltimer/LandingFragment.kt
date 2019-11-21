package com.example.intervaltimer

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
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
import com.example.viewmodel.WorkoutViewModel

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
    val NOTIFICATION_ID = 0

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

            createNotification()
        }

        val viewModel = ViewModelProviders.of(this).get(WorkoutViewModel::class.java)

        /*
        viewModel.insertWorkout(Workout(null, "Chest", 1200, true))
        viewModel.insertWorkout(Workout(null, "Running", 1604, false))
        viewModel.insertWorkout(Workout(null, "Biceps", 1500, false))
        viewModel.insertWorkout(Workout(null, "Triceps", 1130, true))
        viewModel.insertWorkout(Workout(null, "Shoulders", 1030, false))
        viewModel.insertWorkout(Workout(null, "Back", 2412, true))
        viewModel.insertWorkout(Workout(null, "Running", 3000, true))
        viewModel.insertWorkout(Workout(null, "Grip Training", 800, false))
        */

        recyclerLayout = LinearLayoutManager(this.context)
        recyclerAdapter = WorkoutCardAdapter(this)

        // Populate the RecyclerView
        viewModel.getAllWorkouts().observe(this,
            Observer<List<Workout>> { workouts ->
                recyclerAdapter?.setWorkouts(workouts)
            }
        )

        rootView.findViewById<RecyclerView>(R.id.cardRecyclerView).apply {
            layoutManager = recyclerLayout
            adapter = recyclerAdapter
        }
        return rootView
    }

    // TODO: Move to timer screen

    /**
     * Creates a notification that holds information about the current interval along with media controls
     */
    private fun createNotification() {
        val rewindIntent = Intent(this.context, NotificationButtonReceiver::class.java)
        rewindIntent.action = getString(R.string.notification_rewind_action)

        val pauseIntent = Intent(this.context, NotificationButtonReceiver::class.java)
        pauseIntent.action = getString(R.string.notification_pause_action)

        val fastForwardIntent = Intent(this.context, NotificationButtonReceiver::class.java)
        fastForwardIntent.action = getString(R.string.notification_fast_forward_action)

        val rewindPendingIntent: PendingIntent = PendingIntent.getBroadcast(this.context!!, 0, rewindIntent, 0)
        val pausePendingIntent: PendingIntent = PendingIntent.getBroadcast(this.context!!, 1, pauseIntent, 0)
        val fastForwardPendingIntent: PendingIntent = PendingIntent.getBroadcast(this.context!!, 2, fastForwardIntent, 0)

        val builder = NotificationCompat.Builder(this.context!!, getString(R.string.notification_channel))
            .setSmallIcon(R.drawable.ic_plus)
            .setContentTitle("Arm Workout") // TODO: Get current workout name
            .setContentText("Bicep Curls") // TODO: Get current interval name
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(R.drawable.ic_rewind, "Rewind", rewindPendingIntent)
            .addAction(R.drawable.ic_pause, "Pause", pausePendingIntent)
            .addAction(R.drawable.ic_fast_forward, "Fast Forward", fastForwardPendingIntent)


        with(NotificationManagerCompat.from(this.context!!)) {

            builder.setProgress(100, 25, false) // TODO: Get current workout (or interval) progress
            // notificationId is a unique int for each notification that you must define
            notify(NOTIFICATION_ID, builder.build())
        }
    }

}
