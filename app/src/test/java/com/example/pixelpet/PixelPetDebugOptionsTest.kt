package com.example.pixelpet

import com.example.pixelpet.avatar.PetAnimationState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PixelPetDebugOptionsTest {
    @Test
    fun parsesCuriousDebugVisualState() {
        assertEquals(
            PetAnimationState.Curious,
            PixelPetDebugOptions.visualStateFrom("curious"),
        )
    }

    @Test
    fun ignoresUnknownDebugVisualState() {
        assertNull(PixelPetDebugOptions.visualStateFrom("sleepy"))
    }
}
