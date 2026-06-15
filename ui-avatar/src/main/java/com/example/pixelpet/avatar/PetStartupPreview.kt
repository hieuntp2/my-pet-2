package com.example.pixelpet.avatar

object PetStartupPreview {
    const val IDLE_STAGE_DURATION_MS = 600L

    val DefaultStageStates: List<PetAnimationState> = listOf(
        PetAnimationState.LookingIdle,
        PetAnimationState.LookingBlink,
        PetAnimationState.Curious,
        PetAnimationState.HappyReward,
    )
}
