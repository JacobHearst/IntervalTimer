package com.example.intervaltimer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.activity_timer.*
import kotlinx.android.synthetic.main.activity_timer.view.*

class TimerFragment : Fragment() {
    private var mediaPlayer: MediaPlayer? = null
    private var buttonSwitch = false
    private var totaltime: Long = 10000 + 1000
    private val countdown: Long = 1000
    private var ctimer: CountDownTimer? = null
    private val NOTIFICATION_ID = 0
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManager

    fun playTimer(){

        ctimer = object: CountDownTimer(totaltime, countdown){

            override fun onTick(millis: Long){
                totaltime = millis
                var minutes = (millis / 1000) / 60
                var seconds = (millis / 1000) % 60

                var updateStringTimer = String.format("%02d:%02d", minutes, seconds)

                timerText.text = updateStringTimer

            }

            override fun onFinish(){
                mediaPlayer?.start()
            }
        }
        ctimer?.start()

    }

    fun pause(){
        ctimer?.cancel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_timer, container, false)

        mediaPlayer = MediaPlayer.create(context, R.raw.bellring)

        setMenuVisibility(false)

        rootView.workoutName.text = "Push ups" // placeholder code for setting text at startup
        rootView.repsText.text = "# of reps 50" // placeholder code for setting text at startup

        rootView.timerText.text = "00:10" // placeholder code for setting text at startup

        rootView.playButton.setOnClickListener{

            if(buttonSwitch == false){
                rootView.playButton.setBackgroundResource(R.drawable.ic_pause)
                playTimer()
                buttonSwitch = true
            }

            else{
                rootView.playButton.setBackgroundResource(R.drawable.ic_play)
                pause()
                buttonSwitch = false
            }

        }

        rootView.fastForwardButton.setOnClickListener{
            mediaPlayer?.stop()
            totaltime = 70000 + 1000
            rootView.workoutName.text = "Curls" // placeholder code for setting text when hitting forward
            rootView.repsText.text = "# of reps 80" // placeholder code for setting text when hitting forward
            rootView.timerText.text = "01:10" // placeholder code for setting text at startup
            rootView.playButton.setBackgroundResource(R.drawable.ic_play)
            pause()
            buttonSwitch = false
        }

        rootView.rewindButton.setOnClickListener{
            mediaPlayer?.stop()
            totaltime = 10000 + 1000
            rootView.workoutName.text = "Push ups" // placeholder code for setting text when hitting back
            rootView.repsText.text = "# of reps 50" // placeholder code for setting text when hitting back
            rootView.timerText.text = "00:10" // placeholder code for setting text at startup
            rootView.playButton.setBackgroundResource(R.drawable.ic_play)
            pause()
            buttonSwitch = false
        }

        rootView.endWorkoutButton.setOnClickListener{
            mediaPlayer?.release()

            findNavController().navigateUp()

            notificationManager.cancel(NOTIFICATION_ID)
            //findNavController().navigate(TimerFragmentDirections.actionTimerFragmentToIntervalListFragment())
        }

        notificationBuilder = NotificationCompat.Builder(this.context!!, getString(R.string.notification_channel))
        notificationManager = activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()

        createNotification()

        // val audioManager: AudioManager = getSystemService(AUDIO_SERVICE) as AudioManager // Audio manager in case we need it

        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        // TODO: Somehow not show the menu...?
    }

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

        notificationBuilder
            .setSmallIcon(R.drawable.ic_plus)
            .setContentTitle("Arm Workout") // TODO: Get current workout name
            .setContentText("Bicep Curls") // TODO: Get current interval name
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(R.drawable.ic_rewind, "Rewind", rewindPendingIntent)
            .addAction(R.drawable.ic_pause, "Pause", pausePendingIntent)
            .addAction(R.drawable.ic_fast_forward, "Fast Forward", fastForwardPendingIntent)


        with(NotificationManagerCompat.from(this.context!!)) {

            notificationBuilder.setProgress(100, 25, false) // TODO: Get current workout (or interval) progress
            // notificationId is a unique int for each notification that you must define
            notify(NOTIFICATION_ID, notificationBuilder.build())
        }
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Default Channel"
            val descriptionText = "Default notification channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(getString(R.string.notification_channel), name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            notificationManager.createNotificationChannel(channel)
        }
    }
}
