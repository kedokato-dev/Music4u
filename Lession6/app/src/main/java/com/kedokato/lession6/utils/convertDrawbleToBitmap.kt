package com.kedokato.lession6.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat

fun resourceIdToBitmap(context: Context, resId: Int): Bitmap {
    val drawable = ContextCompat.getDrawable(context, resId) ?: return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

    if (drawable is BitmapDrawable) {
        drawable.bitmap?.let { return it }
    }

    val width = if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth else 1
    val height = if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight else 1

    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

