package com.example.pixelpet.avatar

import android.content.res.AssetManager
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

data class SpriteSheetImage(
    val image: ImageBitmap,
    val sheet: SpriteSheet,
)

data class PixelPetAssets(
    val idle: SpriteSheetImage?,
    val blink: SpriteSheetImage?,
    val missingAssets: List<String>,
) {
    val hasRequiredSheets: Boolean = idle != null && blink != null
}

object PixelPetAssetLoader {
    const val IDLE_ASSET_PATH = "pet/eyes/looking-idle.png"
    const val BLINK_ASSET_PATH = "pet/eyes/looking-blink.png"
    private const val TAG = "PixelPetAssets"

    fun load(assetManager: AssetManager): PixelPetAssets {
        val missingAssets = mutableListOf<String>()
        val idle = loadSprite(assetManager, IDLE_ASSET_PATH, missingAssets)
        val blink = loadSprite(assetManager, BLINK_ASSET_PATH, missingAssets)

        return PixelPetAssets(
            idle = idle,
            blink = blink,
            missingAssets = missingAssets,
        )
    }

    private fun loadSprite(
        assetManager: AssetManager,
        assetPath: String,
        missingAssets: MutableList<String>,
    ): SpriteSheetImage? {
        val bitmap = try {
            assetManager.open(assetPath).use(BitmapFactory::decodeStream)
        } catch (exception: Exception) {
            Log.w(TAG, "Missing pet eye asset: $assetPath", exception)
            missingAssets += assetPath
            null
        } ?: return null

        val sheet = try {
            SpriteSheet(
                widthPx = bitmap.width,
                heightPx = bitmap.height,
            )
        } catch (exception: IllegalArgumentException) {
            val reason = "$assetPath (${bitmap.width}x${bitmap.height})"
            Log.w(TAG, "Invalid pet eye sprite sheet: $reason", exception)
            missingAssets += reason
            null
        } ?: return null

        return SpriteSheetImage(
            image = bitmap.asImageBitmap(),
            sheet = sheet,
        )
    }
}
