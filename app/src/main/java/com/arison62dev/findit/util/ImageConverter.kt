package com.arison62dev.findit.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.size.Size
import coil3.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.IOException

object ImageConverter {
    private const val COMPRESSION_QUALITY = 90
    private const val MAX_IMAGE_SIZE = 1024 // pixels

    suspend fun uriToByteArray(context: Context, uri: Uri): ByteArray? = withContext(Dispatchers.IO) {
        val loader = ImageLoader(context)
        try {
            val request = ImageRequest.Builder(context)
                .data(uri)
                .size(Size(MAX_IMAGE_SIZE, MAX_IMAGE_SIZE)) // Limite la taille pour éviter les OOM
                .build()

            when (val result = loader.execute(request)) {
                is SuccessResult -> compressBitmap(result.image.toBitmap())
                else -> null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            loader.shutdown()
        }
    }

    private fun compressBitmap(bitmap: Bitmap): ByteArray? {
        return try {
            ByteArrayOutputStream().use { outputStream ->
                if (bitmap.compress(CompressFormat.JPEG, COMPRESSION_QUALITY, outputStream)) {
                    outputStream.toByteArray()
                } else {
                    null
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    suspend fun uriToByteArrayWithFallback(
        context: Context,
        uri: Uri,
        maxAttempts: Int = 2,
        initialQuality: Int = COMPRESSION_QUALITY
    ): ByteArray? {
        var quality = initialQuality
        var attempt = 0

        while (attempt < maxAttempts) {
            try {
                val result = uriToByteArray(context, uri)
                if (result != null) return result

                // Réduire la qualité si la première tentative échoue
                quality = (quality * 0.8).toInt().coerceAtLeast(50)
                attempt++
            } catch (e: Exception) {
                e.printStackTrace()
                attempt++
            }
        }
        return null
    }
}