package com.example.pixelpet.avatar

object PetAnimationClips {
    const val CURIOUS_MAGNIFIER = "curious_magnifier"
    const val HAPPY_REWARD_SPARKLE = "happy_reward_sparkle"

    val CuriousMagnifierFrameDurationsMs: List<Long> = listOf(220L, 180L, 520L, 650L)
    val HappyRewardSparkleFrameDurationsMs: List<Long> = listOf(220L, 180L, 420L, 520L)

    fun curiousMagnifierAnimation(): SpriteAnimation =
        SpriteAnimation.looping(frameDurationsMs = CuriousMagnifierFrameDurationsMs)

    fun happyRewardSparkleAnimation(): SpriteAnimation =
        SpriteAnimation.oneShot(frameDurationsMs = HappyRewardSparkleFrameDurationsMs)
}
