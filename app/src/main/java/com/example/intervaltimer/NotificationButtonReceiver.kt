package com.example.intervaltimer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.Resources

/**
 * Receives input from action buttons in the notification
 */
class NotificationButtonReceiver : BroadcastReceiver {

    // Automatically calls BroadcastReceiver's constructor
    constructor() { }

    /**
     * Called when a notification's action button is pressed
     *
     * @param context Context of the button press
     * @param intent The pressed button's intent
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action

        // I get a "resource not found" when trying to get the string from the system.
        // For now, this works.
        if(action == "REWIND") {//Resources.getSystem().getString(R.string.notification_rewind_action)) {
            println("TODO: Implement interval rewind")
        } else if (action == "PAUSE") {//Resources.getSystem().getString(R.string.notification_pause_action)) {
            println("TODO: Implement interval pause")
        } else if (action == "FAST FORWARD") {//Resources.getSystem().getString(R.string.notification_fast_forward_action)) {
            println("TODO: Implement interval fast forward")
        }
    }

}