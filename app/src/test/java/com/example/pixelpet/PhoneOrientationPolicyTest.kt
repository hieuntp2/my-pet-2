package com.example.pixelpet

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PhoneOrientationPolicyTest {
    @Test
    fun showsRotateBlockerForPhonePortrait() {
        assertTrue(
            PhoneOrientationPolicy.shouldShowRotateBlocker(
                screenWidthDp = 393,
                screenHeightDp = 852,
            ),
        )
    }

    @Test
    fun allowsPhoneLandscapeMainExperience() {
        assertFalse(
            PhoneOrientationPolicy.shouldShowRotateBlocker(
                screenWidthDp = 852,
                screenHeightDp = 393,
            ),
        )
    }

    @Test
    fun doesNotBlockTabletPortrait() {
        assertFalse(
            PhoneOrientationPolicy.shouldShowRotateBlocker(
                screenWidthDp = 800,
                screenHeightDp = 1280,
            ),
        )
    }
}
