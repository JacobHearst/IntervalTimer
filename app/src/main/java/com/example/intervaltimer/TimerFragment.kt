package com.example.intervaltimer

import android.content.res.Resources
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
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
            //findNavController().navigate(TimerFragmentDirections.actionTimerFragmentToIntervalListFragment())
        }


        // val audioManager: AudioManager = getSystemService(AUDIO_SERVICE) as AudioManager // Audio manager in case we need it

        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        // TODO: Somehow not show the menu...?
    }
}
