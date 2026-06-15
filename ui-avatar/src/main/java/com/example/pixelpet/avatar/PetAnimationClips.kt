package com.example.pixelpet.avatar

import android.content.res.AssetManager
import android.content.res.Resources
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject

class PetAnimationClips private constructor(
    private val clipsById: Map<String, PetAnimationClip>,
) {
    fun animationFor(clipId: String): SpriteAnimation =
        requireNotNull(clipsById[clipId]) { "Missing animation clip: $clipId." }.animation

    companion object {
        const val CURIOUS_MAGNIFIER = "curious_magnifier"
        const val HAPPY_REWARD_SPARKLE = "happy_reward_sparkle"
        private const val TAG = "PetAnimationClips"
        private const val ASSET_PREFIX = "pet/eyes/"
        private const val ASSET_EXTENSION = ".png"

        fun load(resources: Resources, assetManager: AssetManager): PetAnimationClips = try {
            val json = resources.openRawResource(R.raw.pet_animation_config).bufferedReader().use { it.readText() }
            fromJson(json, assetManager)
        } catch (exception: Exception) {
            Log.e(TAG, "Unable to load pet animation JSON config; using safe fallback clips.", exception)
            fallback()
        }

        fun fromJson(json: String, assetManager: AssetManager? = null): PetAnimationClips {
            val root = JSONObject(json)
            val clips = root.getJSONArray("clips")
            val parsedClips = buildMap {
                for (index in 0 until clips.length()) {
                    val clip = clips.getJSONObject(index).toClip(assetManager)
                    put(clip.id, clip)
                }
            }
            require(parsedClips.containsKey(CURIOUS_MAGNIFIER)) { "Config missing required clip: $CURIOUS_MAGNIFIER." }
            require(parsedClips.containsKey(HAPPY_REWARD_SPARKLE)) { "Config missing required clip: $HAPPY_REWARD_SPARKLE." }
            return PetAnimationClips(parsedClips)
        }

        fun fallback(): PetAnimationClips = PetAnimationClips(
            mapOf(
                CURIOUS_MAGNIFIER to PetAnimationClip(
                    id = CURIOUS_MAGNIFIER,
                    asset = CURIOUS_MAGNIFIER,
                    animation = SpriteAnimation.looping(frameDurationsMs = listOf(220L, 180L, 520L, 650L)),
                ),
                HAPPY_REWARD_SPARKLE to PetAnimationClip(
                    id = HAPPY_REWARD_SPARKLE,
                    asset = HAPPY_REWARD_SPARKLE,
                    animation = SpriteAnimation.oneShot(frameDurationsMs = listOf(220L, 180L, 420L, 520L)),
                ),
            ),
        )

        private fun JSONObject.toClip(assetManager: AssetManager?): PetAnimationClip {
            val id = getString("id")
            val asset = getString("asset")
            val frameWidth = optInt("frameWidth", SpriteSheet.FRAME_WIDTH_PX)
            val frameHeight = optInt("frameHeight", SpriteSheet.FRAME_HEIGHT_PX)
            val frameCount = getInt("frameCount")
            val frameSequence = getJSONArray("frameSequence").toIntList()
            val frameDurationsMs = getJSONArray("frameDurationsMs").toLongList()
            val loops = optBoolean("loop", false)
            val holdFinalFrame = optBoolean("holdFinalFrame", !loops)
            val nextStage = optString("nextStage").ifBlank { null }
            val replayCooldownMs = optLongOrNull("replayCooldownMs")
            val priority = optIntOrNull("priority")

            require(frameWidth > 0) { "Clip $id frameWidth must be > 0." }
            require(frameHeight > 0) { "Clip $id frameHeight must be > 0." }
            require(frameCount > 0) { "Clip $id frameCount must be > 0." }
            require(frameSequence.isNotEmpty()) { "Clip $id frameSequence must not be empty." }
            require(frameDurationsMs.isNotEmpty()) { "Clip $id frameDurationsMs must not be empty." }
            require(frameSequence.size == frameDurationsMs.size) { "Clip $id frameSequence and frameDurationsMs sizes must match." }
            require(frameDurationsMs.all { it > 0L }) { "Clip $id frameDurationsMs values must be > 0." }
            require(frameSequence.all { it in 0 until frameCount }) { "Clip $id frameSequence contains an index outside 0..${frameCount - 1}." }
            assetManager?.open(assetPathFor(asset))?.close()

            return PetAnimationClip(
                id = id,
                asset = asset,
                animation = SpriteAnimation(
                    frameSequence = frameSequence,
                    frameDurationsMs = frameDurationsMs,
                    loops = loops,
                    holdFinalFrame = holdFinalFrame,
                ),
                frameWidth = frameWidth,
                frameHeight = frameHeight,
                frameCount = frameCount,
                nextStage = nextStage,
                replayCooldownMs = replayCooldownMs,
                priority = priority,
            )
        }

        private fun assetPathFor(asset: String): String = ASSET_PREFIX + asset + ASSET_EXTENSION

        private fun JSONArray.toIntList(): List<Int> = List(length()) { getInt(it) }
        private fun JSONArray.toLongList(): List<Long> = List(length()) { getLong(it) }
        private fun JSONObject.optLongOrNull(name: String): Long? = if (has(name)) getLong(name) else null
        private fun JSONObject.optIntOrNull(name: String): Int? = if (has(name)) getInt(name) else null
    }
}

data class PetAnimationClip(
    val id: String,
    val asset: String,
    val animation: SpriteAnimation,
    val frameWidth: Int = SpriteSheet.FRAME_WIDTH_PX,
    val frameHeight: Int = SpriteSheet.FRAME_HEIGHT_PX,
    val frameCount: Int = animation.frameCount,
    val nextStage: String? = null,
    val replayCooldownMs: Long? = null,
    val priority: Int? = null,
)
