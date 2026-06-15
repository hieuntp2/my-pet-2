package com.example.pixelpet

import com.example.pixelpet.avatar.PetAnimationState

object PixelPetDebugOptions {
    const val EXTRA_VISUAL_STATE = "pixelpet.debug_visual_state"

    fun visualStateFrom(rawValue: String?): PetAnimationState? = when (rawValue?.trim()?.lowercase()) {
        "curious", PetAnimationState.Curious.clipName -> PetAnimationState.Curious
        else -> null
    }
}
