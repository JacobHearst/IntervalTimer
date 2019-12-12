package com.example.intervaltimer

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.os.CountDownTimer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.android.synthetic.main.activity_timer.*
import android.content.IntentFilter



/**
 * Receives input from action buttons in the notification
 */

class NotificationButtonReceiver : BroadcastReceiver() {
    /**
     * Called when a notification's action button is pressed
     *
     * @param context Context of the button press
     * @param intent The pressed button's intent
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        val actionIntent = Intent("notification.action.intent")
        actionIntent.action = intent?.action
        LocalBroadcastManager.getInstance(context!!).sendBroadcast(actionIntent);
    }
}

class ActionReceiver : BroadcastReceiver {

    private var activity: TimerActivity?

    constructor(activity: TimerActivity) : super() {
        this.activity = activity
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action

        if (action == "REWIND") {
            TimerActivity.forwardBackward(TimerActivity.WorkoutActions.REWIND, activity!!)
            activity!!.playButton.isChecked = false
        } else if (action == "PAUSE") {

            val isChecked = activity!!.playButton.isChecked

            if(isChecked) {
                TimerActivity.pause()
                activity!!.playButton.isChecked = !isChecked
                activity!!.playButton.setBackgroundResource(R.drawable.ic_play)
            } else {
                TimerActivity.play(activity!!)
                activity!!.playButton.isChecked = !isChecked
                activity!!.playButton.setBackgroundResource(R.drawable.ic_pause)
            }
        } else if(action == "PLAY") {
            println("BIG OLE PLAY")
        }
        else if (action == "FAST FORWARD") {
            TimerActivity.forwardBackward(TimerActivity.WorkoutActions.FAST_FORWARD, activity!!)
            activity!!.playButton.isChecked = true
        }

        TimerActivity.updateNotification(activity!!)
    }
}