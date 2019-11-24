package com.example.intervaltimer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import kotlinx.android.synthetic.main.activity_timer.*

class TimerActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private var buttonSwitch = false
    private var totaltime: Long = 10000 + 1000
    private val countdown: Long = 1000
    private var ctimer: CountDownTimer? = null

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_timer)

        val actionBar = supportActionBar
        actionBar!!.setHomeButtonEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)

        mediaPlayer = MediaPlayer.create(this, R.raw.bellring)

        workoutName.text = "Push ups" // placeholder code for setting text at startup
        repsText.text = "# of reps 50" // placeholder code for setting text at startup

        timerText.text = "00:10" // placeholder code for setting text at startup

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
            mediaPlayer?.stop()
            totaltime = 70000 + 1000
            workoutName.text = "Curls" // placeholder code for setting text when hitting forward
            repsText.text = "# of reps 80" // placeholder code for setting text when hitting forward
            timerText.text = "01:10" // placeholder code for setting text at startup
            playButton.setBackgroundResource(R.drawable.ic_play)
            pause()
            buttonSwitch = false
        }

        rewindButton.setOnClickListener{
            mediaPlayer?.stop()
            totaltime = 10000 + 1000
            workoutName.text = "Push ups" // placeholder code for setting text when hitting back
            repsText.text = "# of reps 50" // placeholder code for setting text when hitting back
            timerText.text = "00:10" // placeholder code for setting text at startup
            playButton.setBackgroundResource(R.drawable.ic_play)
            pause()
            buttonSwitch = false
        }

        endWorkoutButton.setOnClickListener{
            mediaPlayer?.release()
            mediaPlayer = null
        }


        // val audioManager: AudioManager = getSystemService(AUDIO_SERVICE) as AudioManager // Audio manager in case we need it

    }

    override fun onSupportNavigateUp(): Boolean {
        //findNavController().navigate()
        return false
    }
}
