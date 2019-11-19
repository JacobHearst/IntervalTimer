package com.example.intervaltimer

import org.junit.Test

/**
 * Unit testing for the Utility class
 */
class UtilTest {
    /**
     * Ensure that the conversion from seconds to a time label is correct
     */
    @Test
    fun getDurationLabel() {
        // Helper class to reduce code length
        data class DurationTestValue(val duration: Int, val expectedLabel: String)

        val testCases = arrayOf(
            DurationTestValue(300, "5:00"),
            DurationTestValue(0, "0:00"),
            DurationTestValue(165, "2:45"),
            DurationTestValue(10, "0:10"),
            DurationTestValue(9, "0:09"),
            DurationTestValue(3, "0:03"),
            DurationTestValue(4588, "1:16:28")
        )

        testCases.forEach {
            assert(Util.getDurationLabel(it.duration) == it.expectedLabel)
        }

    }
}
