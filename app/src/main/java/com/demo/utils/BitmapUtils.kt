package com.demo.utils

import android.graphics.Bitmap
import android.graphics.Matrix

object BitmapUtils {
    fun getResizedBitmap(bm: Bitmap?, newWidth: Float, newHeight: Float): Bitmap? {
        val bitmapCopy = bm?.copy(bm.config, false)
        if (newWidth > 0f && newHeight > 0f) {
            if (bitmapCopy == null) return bitmapCopy
            val width = bitmapCopy.width
            val height = bitmapCopy.height
            val scaleWidth = newWidth / width
            val scaleHeight = newHeight / height
            val matrix = Matrix()
            matrix.postScale(scaleWidth, scaleHeight)
            return Bitmap.createBitmap(bitmapCopy, 0, 0, width, height, matrix, true)
        } else {
            return bitmapCopy
        }
    }
}