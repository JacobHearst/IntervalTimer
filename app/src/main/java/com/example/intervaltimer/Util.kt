package com.example.intervaltimer

class Util {
    companion object {
        fun getDurationLabel(length: Int): String {
            val hours: Int = (length / 3600) % 60
            val minutes: Int = (length / 60) % 60
            val seconds = length % 60

            val returnString = StringBuilder()

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