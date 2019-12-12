package com.example.intervaltimer

import android.content.res.ColorStateList
import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import androidx.core.view.isVisible
import androidx.navigation.navArgs
import kotlinx.android.synthetic.main.activity_timer.*


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
    private var buttonSwitch = false
    private var totaltime: Long = 0
    private val countdown: Long = 1000
    private var intervalCountdowntTimer: CountDownTimer? = null
    private var intervalListIterator: Int = 0
    private val args: TimerActivityArgs by navArgs()
    private var endOfList = false

    /**
     * Creates a timer and calls the on tick function
     * of the timer.
     */

    fun playTimer(){

        // Creates a countdown timer object
        intervalCountdowntTimer = object: CountDownTimer(totaltime, countdown){

            /**
             * Overrides the onTick function of the countdown timer
             * @param millis the total time of the countdown timer
             */

            override fun onTick(millis: Long){
                totaltime = millis


                workoutProgressBar.incrementProgressBy(1000)

                var minutes = (millis / 1000) / 60
                var seconds = (millis / 1000) % 60

                var updateStringTimer = String.format("%02d:%02d", minutes, seconds)

                timerText.text = updateStringTimer

            }

            /**
             * Overrides the onFinish function to call the resetTimer function
             */

            override fun onFinish(){
                resetTimer()
            }
        }
        // Starts the countdown timer
        intervalCountdowntTimer?.start()

    }

    /**
     * Calls the cancel function of the created timer
     */

    private fun pause(){
        intervalCountdowntTimer?.cancel()
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

        val actionBar = supportActionBar
        actionBar!!.setHomeButtonEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)

        forwardbackword(0)

        playButton.setOnClickListener{

            if(buttonSwitch == false){
                playButton.setBackgroundResource(R.drawable.ic_pause)
                playTimer()
                buttonSwitch = true
            }

            else{
                playButton.setBackgroundResource(R.drawable.ic_play)
                pause()
                buttonSwitch = false
            }

        }

        fastForwardButton.setOnClickListener{
            playButton.setBackgroundResource(R.drawable.ic_pause)
            buttonSwitch = true


            forwardbackword(1)
        }

        rewindButton.setOnClickListener{
            endOfList = false
            forwardbackword(-1)
        }

        endWorkoutButton.setOnClickListener{
            intervalCountdowntTimer?.cancel()
            mediaPlayer?.release()
            mediaPlayer = null
            finish()
        }

    }

    /** Handles the onclick events of the back and forward buttons
     * Used to set the UI of the layout to reflect the previous or next interval in the list
     * @param nav the amount to increase itera by to traverse the list of intervals
     */

    fun forwardbackword(nav: Int){

        if (intervalListIterator == 0 && nav == -1){
            // Checks to see if when going back in the list at the start does not decrease the list index to be negative

        } else if(intervalListIterator == (args.intervalList.List.size - 1) && nav != -1){
            // Checks to see if the interval list is at the end and to not increase the list index to be more than the size of the list

        }else{
            intervalListIterator  = intervalListIterator + nav
        }

        // Sets the backwards button state
        if(intervalListIterator == 0){
            rewindButton.setBackgroundResource(R.drawable.ic_fast_rewind_gray_24dp)
            rewindButton.isEnabled = false
        }else{
            rewindButton.setBackgroundResource(R.drawable.ic_rewind)
            rewindButton.isEnabled = true
        }

        // Sets the forwards button state
        if(intervalListIterator == (args.intervalList.List.size - 1)){
            fastForwardButton.setBackgroundResource(R.drawable.ic_fast_forward_gray_24dp)
            fastForwardButton.isEnabled = false
        }else{
            fastForwardButton.setBackgroundResource(R.drawable.ic_fast_forward)
            fastForwardButton.isEnabled = true
        }

        workoutName.text = args.intervalList.List[intervalListIterator].name

        // Checks if reps is null
        if (args.intervalList.List[intervalListIterator].reps != null){
            repsText.text = "Reps: " + args.intervalList.List[intervalListIterator].reps.toString()
        } else {
            repsText.text = ""
        }

        // Checks if timer is null
        if( args.intervalList.List[intervalListIterator].time != null){
            var intervalTimeInSeconds: Int?
            intervalTimeInSeconds = args.intervalList.List[intervalListIterator].time

            var minutes = intervalTimeInSeconds!! / 60
            var seconds = intervalTimeInSeconds % 60


            val covertTimeText = String.format("%02d:%02d", minutes, seconds)

            totaltime = (minutes * 60000 + seconds * 1000 + 1000).toLong()

            timerText.text = covertTimeText

            workoutProgressBar.max = totaltime.toInt()
            workoutProgressBar.setProgress(0)
        } else {
            timerText.text = "00:00"
            totaltime = 0
        }

        var intervalColorText: Int

        // Checks if no color is selected
        if (args.intervalList.List[intervalListIterator].color == "00000000"){

            intervalColorText = Color.parseColor("White")

            timerText.setTextColor(intervalColorText)
            repsText.setTextColor(intervalColorText)
            workoutName.setTextColor(intervalColorText)
            workoutProgressBar.progressTintList = ColorStateList.valueOf(intervalColorText)

        }else{

            intervalColorText = Color.parseColor("#" + args.intervalList.List[intervalListIterator].color)

            timerText.setTextColor(intervalColorText)
            repsText.setTextColor(intervalColorText)
            workoutName.setTextColor(intervalColorText)
            workoutProgressBar.progressTintList = ColorStateList.valueOf(intervalColorText)
        }

        // if hitting the rewind button while the timer is counting down the reset timer will be paused
        if(nav == -1){
            pause()
            playButton.setBackgroundResource(R.drawable.ic_play)
            buttonSwitch = false
            workoutProgressBar.setProgress(0)
        }

        // if hitting the forward button while the timer is counting down the timer will move move on to the next one
        if(nav == 1){
            pause()

            // Checks if the timer is 0
            if(args.intervalList.List[intervalListIterator].time != null){
                playTimer()
            } else {
                playButton.setBackgroundResource(R.drawable.ic_play)
                buttonSwitch = false
            }

            workoutProgressBar.setProgress(0)
        }

    }

    /**
     * Handles the event of the back arrow being hit
     * Returns false
     */

    override fun onSupportNavigateUp(): Boolean {
        intervalCountdowntTimer?.cancel()
        mediaPlayer?.release()
        mediaPlayer = null
        finish()
        return false
    }

    /**
     * Resets the timer and creates the next media player to play the next sound
     * when the timer ends
     */

    fun resetTimer(){

        // Plays the sound clip once if at the end of the list or if the timer is 0
        if (args.intervalList.List[intervalListIterator].time != null && endOfList == false){
            mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_NOTIFICATION_URI)
            mediaPlayer?.start()
        }

        if(intervalListIterator != args.intervalList.List.size - 1) {

            forwardbackword(1)
            endOfList = false

        } else{
            endOfList = true
        }



    }
}
