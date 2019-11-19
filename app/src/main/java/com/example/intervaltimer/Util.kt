package com.example.intervaltimer

class Util {
    companion object {
        fun getDurationLabel(length: Int): String {
            val hours: Int = (length / 3600) % 60
            val minutes: Int = (length / 60) % 60
            val seconds = length % 60
            // Add a 0 in front of the seconds if it's < 10
            // Turns this: 1:3 to this: 1:0

            var returnString = StringBuilder()

            if(hours > 0) {
                returnString.append("${hours}:")
            }

            if(minutes < 10) {
                if(hours == 0) {
                    returnString.append("${minutes}:")
                }
                else {
                    returnString.append("0${minutes}:")
                }
            } else {
                returnString.append("${minutes}:")
            }

            if(seconds < 10) {
                returnString.append("0${seconds}")
            } else {
                returnString.append("${seconds}")
            }

            return returnString.toString()
        }
    }
}