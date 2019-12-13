package com.example.intervaltimer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.ColorStateList
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.isVisible
import androidx.databinding.adapters.SeekBarBindingAdapter.setProgress
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.navArgs
import com.example.room.Interval
import kotlinx.android.synthetic.main.activity_timer.*
import org.w3c.dom.Text


/**
 * Activity for the timer screen that runs through the intervals of
 * a workout group
 * @property mediaPlayer the media player for playing sound
 * @property buttonSwitch keeps track of the on and off state of the play button
 * @property totaltime the total time of a interval
 * @property countdown the countdown rate of the timer
 * @property intervalCountdowntTimer the count down timer
 * @property intervalListIterator the iterator of the list of intervals
 * @property args the receiving variable of the navigation arguments
 * @property endOfList used to play the sound clip one time at the end of the list
 */

class TimerActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private val args: TimerActivityArgs by navArgs()
    private var endOfList = false

    /*
     * Notification builders, intents, and more.
     */
    private val NOTIFICATION_ID = 0
    private lateinit var rewindIntent: Intent
    private lateinit var pauseIntent: Intent
    private lateinit var playIntent: Intent
    private lateinit var fastForwardIntent: Intent
    private lateinit var rewindPendingIntent: PendingIntent
    private lateinit var pausePendingIntent: PendingIntent
    private lateinit var playPendingIntent: PendingIntent
    private lateinit var fastForwardPendingIntent: PendingIntent

    enum class WorkoutActions {
        PAUSE,
        PLAY,
        REWIND,
        FAST_FORWARD
    }

    /** Overrides the onCreate function
     *
     * @param savedInstanceState sets the new view of the layout
     * and assigns listeners for buttons
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_timer)

        invalidateOptionsMenu()

        val actionBar = supportActionBar!!
        actionBar.setHomeButtonEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setTitle(R.string.timer_activity_title)

        playButton.setOnClickListener {

            if (playButton.isChecked) {
                playButton.setBackgroundResource(R.drawable.ic_pause)
                play(this)
            } else {
                playButton.setBackgroundResource(R.drawable.ic_play)
                pause()
            }

        }

        fastForwardButton.setOnClickListener {
            playButton.setBackgroundResource(R.drawable.ic_pause)
            forwardBackward(WorkoutActions.FAST_FORWARD, this)
        }

        rewindButton.setOnClickListener {
            endOfList = false
            forwardBackward(WorkoutActions.REWIND, this)
        }

        endWorkoutButton.setOnClickListener {
            intervalCountdownTimer?.cancel()
            mediaPlayer?.release()
            mediaPlayer = null
            notificationManager.cancel(NOTIFICATION_ID)
            finish()
        }

        // Create intents for each button in the notification
        rewindIntent = Intent(this, NotificationButtonReceiver::class.java)
        pauseIntent = Intent(this, NotificationButtonReceiver::class.java)
        playIntent = Intent(this, NotificationButtonReceiver::class.java)
        fastForwardIntent = Intent(this, NotificationButtonReceiver::class.java)

        // Assign an action to each button
        fastForwardIntent.action = getString(R.string.notification_fast_forward_action)
        rewindIntent.action = getString(R.string.notification_rewind_action)
        playIntent.action = "PLAY"
        pauseIntent.action = getString(R.string.notification_pause_action)

        // Create a pending intent for each button, used for receiving a button press
        rewindPendingIntent = PendingIntent.getBroadcast(this, 0, rewindIntent, 0)
        pausePendingIntent = PendingIntent.getBroadcast(this, 1, pauseIntent, 0)
        playPendingIntent = PendingIntent.getBroadcast(this, 2, playIntent, 0)
        fastForwardPendingIntent = PendingIntent.getBroadcast(this, 3, fastForwardIntent, 0)

        // Create the notification manager and builder.
        notificationBuilder = NotificationCompat.Builder(this, getString(R.string.notification_channel))
        notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()
        updateNotification(this)

        // Update the notification with information about the first interval
        notificationBuilder.apply {
            setProgress(100, 0, false)
            setContentTitle(args.intervalList.List.get(0).name)
            setContentText(Util.getDurationLabel(args.intervalList.List.get(0).time!!))
        }

        // Notify the notification to change
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())

        // Create a receiver for updating the UI based on notification button presses
        val receiver = ActionReceiver(this)
        val filter = IntentFilter()
        filter.addAction("REWIND")
        filter.addAction("PAUSE")
        filter.addAction("PLAY")
        filter.addAction("FAST FORWARD")
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter)

        // Queue the first interval to be displayed
        forwardBackward(WorkoutActions.PAUSE, this)
    }

    /** Handles the onclick events of the back and forward buttons
     * Used to set the UI of the layout to reflect the previous or next interval in the list
     * @param nav the amount to increase itera by to traverse the list of intervals
     */

    /**
     * Handles the event of the back arrow being hit
     * Returns false
     */

    override fun onSupportNavigateUp(): Boolean {
        intervalCountdownTimer?.cancel()
        notificationManager.cancel(NOTIFICATION_ID)
        mediaPlayer?.release()
        mediaPlayer = null
        finish()
        return false
    }

    /**
     * Resets the timer and creates the next media player to play the next sound
     * when the timer ends
     */

    fun resetTimer() {

        // Plays the sound clip once if at the end of the list or if the timer is 0
        if (args.intervalList.List[intervalListIterator].time != null && endOfList == false) {
            mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_NOTIFICATION_URI)
            mediaPlayer?.start()
        }

        if (intervalListIterator != args.intervalList.List.size - 1) {
            forwardBackward(WorkoutActions.FAST_FORWARD, this)
            endOfList = false
        } else {
            endOfList = true
        }
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Workout App"
            val descriptionText = "Notification channel for the workout app."
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                getString(R.string.notification_channel),
                name,
                importance
            ).apply {
                description = descriptionText
            }

            channel.setSound(null, null)
            // Register the channel with the system
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {

        var intervalCountdownTimer: CountDownTimer? = null
        var intervalListIterator: Int = 0
        private var totaltime: Long = 0
        private val countdown: Long = 1000

        private lateinit var notificationBuilder: NotificationCompat.Builder
        private lateinit var notificationManager: NotificationManager

        fun pause() {
            intervalCountdownTimer?.cancel()
        }

        /**
         * Creates a timer and calls the on tick function
         * of the timer.
         */

        fun play(activity: TimerActivity) {

            // Creates a countdown timer object
            intervalCountdownTimer = object : CountDownTimer(totaltime, countdown) {

                /**
                 * Overrides the onTick function of the countdown timer
                 * @param millis the total time of the countdown timer
                 */

                override fun onTick(millis: Long) {
                    totaltime = millis
                    activity.workoutProgressBar.incrementProgressBy(1000)
                    val minutes = (millis / 1000) / 60
                    val seconds = (millis / 1000) % 60
                    val updateStringTimer = String.format("%02d:%02d", minutes, seconds)
                    activity.timerText.text = updateStringTimer
                    updateNotification(activity)
                }

                /**
                 * Overrides the onFinish function to call the resetTimer function
                 */

                override fun onFinish() {
                    activity.resetTimer()
                }
            }
            // Starts the countdown timer
            intervalCountdownTimer?.start()
        }

        fun updateNotification(activity: TimerActivity) {
            notificationBuilder.apply {
                setSmallIcon(R.drawable.ic_stopwatch)
                setProgress(totaltime.toInt(), activity.workoutProgressBar.progress, false)
                setContentTitle(activity.workoutName.text)
                setContentText(activity.timerText.text)
                addAction(R.drawable.ic_rewind, "Rewind", activity.rewindPendingIntent)
                notificationBuilder.addAction(R.drawable.ic_pause, "Play / Pause", activity.pausePendingIntent)
                addAction(R.drawable.ic_fast_forward, "Fast Forward", activity.fastForwardPendingIntent)
                setShowWhen(false)
            }

            with(NotificationManagerCompat.from(activity)) {
                notify(activity.NOTIFICATION_ID, notificationBuilder.build())
            }
        }

        fun forwardBackward(nav: WorkoutActions, activity: TimerActivity) {

            val intervalListSize = activity.args.intervalList.List.size

            if (intervalListIterator == 0 && nav == WorkoutActions.REWIND) {
                // Checks to see if when going back in the list at the start does not decrease the list index to be negative

            } else if (intervalListIterator == (intervalListSize - 1) && nav != WorkoutActions.REWIND) {
                // Checks to see if the interval list is at the end and to not increase the list index to be more than the size of the list

            } else {
                if(nav == WorkoutActions.FAST_FORWARD) {
                    intervalListIterator++
                }
                else if(nav == WorkoutActions.REWIND){
                    intervalListIterator--
                }
            }

            // Sets the backwards button state
            if (intervalListIterator == 0) {
                activity.rewindButton.setBackgroundResource(R.drawable.ic_fast_rewind_gray_24dp)
                activity.rewindButton.isEnabled = false
            } else {
                activity.rewindButton.setBackgroundResource(R.drawable.ic_rewind)
                activity.rewindButton.isEnabled = true
            }

            // Sets the forwards button state
            if (intervalListIterator == (activity.args.intervalList.List.size - 1)) {
                activity.fastForwardButton.setBackgroundResource(R.drawable.ic_fast_forward_gray_24dp)
                activity.fastForwardButton.isEnabled = false
            } else {
                activity.fastForwardButton.setBackgroundResource(R.drawable.ic_fast_forward)
                activity.fastForwardButton.isEnabled = true
            }

            activity.workoutName.text = activity.args.intervalList.List[intervalListIterator].name

            // Checks if reps is null
            if (activity.args.intervalList.List[intervalListIterator].reps != null) {
                activity.repsText.text = "Reps: " + activity.args.intervalList.List[intervalListIterator].reps.toString()
            } else {
                activity.repsText.text = ""
            }

            // Checks if timer is null
            if (activity.args.intervalList.List[intervalListIterator].time != null) {
                var intervalTimeInSeconds: Int?
                intervalTimeInSeconds = activity.args.intervalList.List[intervalListIterator].time

                var minutes = intervalTimeInSeconds!! / 60
                var seconds = intervalTimeInSeconds % 60


                val covertTimeText = String.format("%02d:%02d", minutes, seconds)

                totaltime = (minutes * 60000 + seconds * 1000 + 1000).toLong()

                activity.timerText.text = covertTimeText

                activity.workoutProgressBar.max = totaltime.toInt()
                activity.workoutProgressBar.setProgress(0)
            } else {
                activity.timerText.text = "00:00"
                totaltime = 0
            }

            var intervalColorText: Int

            // Checks if no color is selected
            if (activity.args.intervalList.List[intervalListIterator].color == "00000000") {

                intervalColorText = Color.parseColor("White")

                activity.timerText.setTextColor(intervalColorText)
                activity.repsText.setTextColor(intervalColorText)
                activity.workoutName.setTextColor(intervalColorText)
                activity.workoutProgressBar.progressTintList = ColorStateList.valueOf(intervalColorText)

            } else {

                intervalColorText =
                    Color.parseColor("#" + activity.args.intervalList.List[intervalListIterator].color)

                activity.timerText.setTextColor(intervalColorText)
                activity.repsText.setTextColor(intervalColorText)
                activity.workoutName.setTextColor(intervalColorText)
                activity.workoutProgressBar.progressTintList = ColorStateList.valueOf(intervalColorText)
            }

            // if hitting the rewind button while the timer is counting down the reset timer will be paused
            if (nav == WorkoutActions.REWIND) {
                pause()
                activity.playButton.setBackgroundResource(R.drawable.ic_play)
                activity.workoutProgressBar.setProgress(0)
            }

            // if hitting the forward button while the timer is counting down the timer will move move on to the next one
            if (nav == WorkoutActions.FAST_FORWARD) {
                pause()

                // Checks if the timer is 0
                if (activity.args.intervalList.List[intervalListIterator].time != null) {
                    play(activity)
                } else {
                    activity.playButton.setBackgroundResource(R.drawable.ic_play)
                }

                activity.workoutProgressBar.setProgress(0)
            }

            updateNotification(activity)
        }
    }
}
