package com.example.pixelpet.avatar

enum class PetAnimationState(val clipName: String) {
    LookingIdle("looking_idle"),
    LookingBlink("looking_blink"),
    Curious(PetAnimationClips.CURIOUS_MAGNIFIER),
    HappyReward(PetAnimationClips.HAPPY_REWARD_SPARKLE),
}
