package com.gig.zendo.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object CloudinaryUploader {

    private val cloudinary = Cloudinary(
        mapOf(
            "cloud_name" to "dlgiukuzl",
            "api_key" to "256968335817433",
            "api_secret" to "ob5-9SfV8LbAxowHSXd9Op_2KqE"
        )
    )

    suspend fun uploadImageFromUri(context: Context, uri: Uri): UiState<String> = withContext(Dispatchers.IO) {
        return@withContext try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)

            inputStream?.use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            val result = cloudinary.uploader().upload(
                tempFile,
                ObjectUtils.asMap("upload_preset", "zendo_upload_images")
            )

            val url = result["secure_url"] as? String
            if (url != null) UiState.Success(url) else UiState.Empty
        } catch (e: Exception) {

            UiState.Failure(e.message)
        }
    }

}