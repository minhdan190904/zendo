package com.gig.zendo.utils

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.foundation.clickable

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.ComposeView
import androidx.core.graphics.createBitmap
import kotlinx.coroutines.suspendCancellableCoroutine
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.resume

fun Long.toMoney(): String {
    return this.toMoneyWithoutUnit() + " đ"
}

fun Long.toMoneyWithoutUnit(): String {
    return String.format(Locale.US, "%,d", this)
}

fun getToday(): String {
    return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
}

fun String.toDate(): Date {
    return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(this) ?: Date()
}

//get first day of this month
fun getFirstDayOfThisMonth(): String {
    return SimpleDateFormat("01/MM/yyyy", Locale.getDefault()).format(Date())
}

fun getCurrentYear(): String {
    return SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())
}

fun getCurrentMonth(): String {
    return SimpleDateFormat("MM", Locale.getDefault()).format(Date())
}

suspend fun Context.captureComposableAsBitmap(
    content: @Composable () -> Unit
): Bitmap = suspendCancellableCoroutine { continuation ->

    val composeView = ComposeView(this).apply {
        setContent {
            content()
        }
        layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    val container = FrameLayout(this).apply {
        addView(composeView)
    }

    val activity = this as? Activity
    val decorView = activity?.window?.decorView as? ViewGroup

    decorView?.post {
        decorView.addView(container)

        composeView.measure(
            View.MeasureSpec.makeMeasureSpec(decorView.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        composeView.layout(
            0, 0,
            composeView.measuredWidth,
            composeView.measuredHeight
        )

        val bitmap = createBitmap(composeView.measuredWidth, composeView.measuredHeight)
        val canvas = Canvas(bitmap)
        composeView.draw(canvas)

        decorView.removeView(container)

        continuation.resume(bitmap)
    }
}



fun saveBitmapToPictures(context: Context, bitmap: Bitmap, folderName: String = "MyScreenshots", fileName: String = "screenshot.png") {
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$folderName")
        put(MediaStore.Images.Media.IS_PENDING, 1)
    }

    val contentResolver = context.contentResolver
    val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

    uri?.let {
        contentResolver.openOutputStream(it)?.use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        }

        contentValues.clear()
        contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
        contentResolver.update(uri, contentValues, null, null)
    }
}

fun Modifier.debounceClickable(
    debounceInterval: Long = 2000L,
    enabled: Boolean = true,
    onClick: () -> Unit
): Modifier = composed {
    var lastClickTime by remember { mutableLongStateOf(0L) }

    this.then(
        Modifier.clickable(enabled = enabled) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime > debounceInterval) {
                lastClickTime = currentTime
                onClick()
            }
        }
    )
}

@Composable
fun rememberDebouncedClick(
    intervalMs: Long = 1000L,
    enabled: Boolean = true,
    onClick: () -> Unit
): () -> Unit {
    val latestOnClick by rememberUpdatedState(onClick)
    var lastClickTime by rememberSaveable { mutableLongStateOf(0L) }

    return {
        if (enabled) {
            val now = System.currentTimeMillis()
            if (now - lastClickTime > intervalMs) {
                lastClickTime = now
                latestOnClick()
            }
        }
    }
}