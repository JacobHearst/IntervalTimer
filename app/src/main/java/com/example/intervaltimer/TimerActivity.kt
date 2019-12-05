package com.example.intervaltimer

import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import androidx.navigation.navArgs
import kotlinx.android.synthetic.main.activity_timer.*

class TimerActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private var buttonSwitch = false
    private var totaltime: Long = 0
    private val countdown: Long = 1000
    private var ctimer: CountDownTimer? = null
    private var itera: Int = 0

    private val args: TimerActivityArgs by navArgs()

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
                resetTimer()
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

        invalidateOptionsMenu()

        workoutName.text = args.intervalList.List[0].name // placeholder code for setting text at startup

        if (args.intervalList.List[0].reps.toString() != "null"){
            repsText.text = args.intervalList.List[0].reps.toString() // placeholder code for setting text at startup
        }

        var covertTime: Int?
        covertTime = args.intervalList.List[0].time

        var minutes = covertTime!! / 60
        var seconds = covertTime % 60


        val covertTimeText = String.format("%02d:%02d", minutes, seconds)

        totaltime = (minutes * 60000 + seconds * 1000 + 1000).toLong()

        timerText.text = covertTimeText

        timerText.setTextColor(Color.parseColor(args.intervalList.List[0].color))
        repsText.setTextColor(Color.parseColor(args.intervalList.List[0].color))
        workoutName.setTextColor(Color.parseColor(args.intervalList.List[0].color))
        workoutProgressBar.setBackgroundColor(Color.parseColor(args.intervalList.List[0].color))

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
            forwardbackword(1)
        }

        rewindButton.setOnClickListener{
            forwardbackword(-1)
        }

        endWorkoutButton.setOnClickListener{
            mediaPlayer?.release()
            mediaPlayer = null
            finish()
        }


        // val audioManager: AudioManager = getSystemService(AUDIO_SERVICE) as AudioManager // Audio manager in case we need it

    }

    fun forwardbackword(nav: Int){

        if (itera == 0 && nav == -1){

        } else if(itera == (args.intervalList.List.size - 1)){

        }else{
            itera += nav
        }

        workoutName.text = args.intervalList.List[itera].name // placeholder code for setting text at startup

        if (args.intervalList.List[0].reps.toString() != "null"){
            repsText.text = args.intervalList.List[itera].reps.toString() // placeholder code for setting text at startup
        }

        var covertTime: Int?
        covertTime = args.intervalList.List[itera].time

        var minutes = covertTime!! / 60
        var seconds = covertTime % 60


        val covertTimeText = String.format("%02d:%02d", minutes, seconds)

        totaltime = (minutes * 60000 + seconds * 1000 + 1000).toLong()

        timerText.text = covertTimeText

        timerText.setTextColor(Color.parseColor(args.intervalList.List[itera].color))
        repsText.setTextColor(Color.parseColor(args.intervalList.List[itera].color))
        workoutName.setTextColor(Color.parseColor(args.intervalList.List[itera].color))
        workoutProgressBar.setBackgroundColor(Color.parseColor(args.intervalList.List[itera].color))

//        mediaPlayer?.stop()
//        totaltime = 70000 + 1000
//        workoutName.text = "Curls" // placeholder code for setting text when hitting forward
//        repsText.text = "# of reps 80" // placeholder code for setting text when hitting forward
//        timerText.text = "01:10" // placeholder code for setting text at startup
        playButton.setBackgroundResource(R.drawable.ic_play)
        pause()
        buttonSwitch = false

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return false
    }

    fun resetTimer(){
        mediaPlayer = MediaPlayer.create(this, R.raw.bellring)
        mediaPlayer?.start()
    }
}
