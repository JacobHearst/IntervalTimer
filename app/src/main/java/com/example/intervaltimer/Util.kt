package com.example.intervaltimer

class Util {
    companion object {
        fun getDurationLabel(length: Int): String {
            val minutes: Int = length / 60
            val seconds = length % 60
            // Add a 0 in front of the seconds if it's < 10
            // Turns this: 1:3 to this: 1:03
            return "$minutes:${if(seconds < 10) "0" else ""}$seconds"
        }
    }
}