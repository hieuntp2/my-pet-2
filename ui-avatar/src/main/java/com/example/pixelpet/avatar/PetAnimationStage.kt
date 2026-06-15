package com.example.pixelpet.avatar

sealed class PetAnimationStage(
    val state: PetAnimationState,
    val animation: SpriteAnimation,
    val canBeInterrupted: Boolean = true,
    val nextStageState: PetAnimationState? = null,
) {
    val totalDurationMs: Long get() = animation.totalDurationMs

    fun frameAt(elapsedMs: Long): Int = animation.frameAt(elapsedMs)

    fun nextStageOnComplete(elapsedMs: Long): PetAnimationState? =
        if (animation.isComplete(elapsedMs)) nextStageState else null
}

class LookingIdleStage(animation: SpriteAnimation) : PetAnimationStage(
    state = PetAnimationState.LookingIdle,
    animation = animation,
    canBeInterrupted = true,
)

class LookingBlinkStage(animation: SpriteAnimation) : PetAnimationStage(
    state = PetAnimationState.LookingBlink,
    animation = animation,
    canBeInterrupted = false,
    nextStageState = PetAnimationState.LookingIdle,
)

class CuriousStage(animation: SpriteAnimation) : PetAnimationStage(
    state = PetAnimationState.Curious,
    animation = animation,
    canBeInterrupted = true,
)

class HappyRewardStage(animation: SpriteAnimation) : PetAnimationStage(
    state = PetAnimationState.HappyReward,
    animation = animation,
    canBeInterrupted = false,
    nextStageState = PetAnimationState.LookingIdle,
)

class PetAnimationStageRegistry(
    private val stages: Map<PetAnimationState, PetAnimationStage>,
) {
    fun stageFor(state: PetAnimationState): PetAnimationStage =
        requireNotNull(stages[state]) { "Missing animation stage for $state." }

    companion object {
        fun create(
            idleAnimation: SpriteAnimation,
            blinkAnimation: SpriteAnimation,
            clips: PetAnimationClips = PetAnimationClips.fallback(),
        ): PetAnimationStageRegistry = PetAnimationStageRegistry(
            mapOf(
                PetAnimationState.LookingIdle to LookingIdleStage(idleAnimation),
                PetAnimationState.LookingBlink to LookingBlinkStage(blinkAnimation),
                PetAnimationState.Curious to CuriousStage(clips.animationFor(PetAnimationClips.CURIOUS_MAGNIFIER)),
                PetAnimationState.HappyReward to HappyRewardStage(clips.animationFor(PetAnimationClips.HAPPY_REWARD_SPARKLE)),
            ),
        )
    }
}
