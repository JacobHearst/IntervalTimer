package com.example.intervaltimer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import kotlinx.android.synthetic.main.activity_timer.*
import kotlin.concurrent.timer

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

                timertext.text = updateStringTimer

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

        workoutname.text = "Push ups" // placeholder code for setting text at startup
        repstext.text = "# of reps 50" // placeholder code for setting text at startup

        timertext.text = "00:10" // placeholder code for setting text at startup

        playbutton.setOnClickListener{

            if(buttonSwitch == false){
                playbutton.setBackgroundResource(R.drawable.ic_pause_black_24dp)
                playTimer()
                buttonSwitch = true
            }

            else{
                playbutton.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp)
                pause()
                buttonSwitch = false
            }

        }

        forwardbutton.setOnClickListener{
            mediaPlayer?.stop()
            totaltime = 70000 + 1000
            workoutname.text = "Curls" // placeholder code for setting text when hitting forward
            repstext.text = "# of reps 80" // placeholder code for setting text when hitting forward
            timertext.text = "01:10" // placeholder code for setting text at startup
            playbutton.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp)
            pause()
            buttonSwitch = false
        }

        backbutton.setOnClickListener{
            mediaPlayer?.stop()
            totaltime = 10000 + 1000
            workoutname.text = "Push ups" // placeholder code for setting text when hitting back
            repstext.text = "# of reps 50" // placeholder code for setting text when hitting back
            timertext.text = "00:10" // placeholder code for setting text at startup
            playbutton.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp)
            pause()
            buttonSwitch = false
        }

        endbutton.setOnClickListener{
            mediaPlayer?.release()
            mediaPlayer = null
        }


        // val audioManager: AudioManager = getSystemService(AUDIO_SERVICE) as AudioManager // Audio manager in case we need it

    }

    override fun onSupportNavigateUp(): Boolean {
        //val navController = this.findNavController(R.id.myNavHostFragment)
        //navController.navigate(R.id.mainFragment)
        return false
    }
}
