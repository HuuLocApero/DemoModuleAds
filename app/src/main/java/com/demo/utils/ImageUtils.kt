package com.demo.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.net.Uri
import android.os.Environment
import android.os.SystemClock
import android.provider.MediaStore
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.demomoduleads.R
import com.demo.common.Constant.AI_GENERATED_PHOTO_FOLDER
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import java.io.File
import java.io.File.separator
import java.io.FileOutputStream
import java.io.OutputStream

object ImageUtils {

    private val shimmer = Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
        .setDuration(1800) // how long the shimmering animation takes to do one full sweep
        .setBaseAlpha(0.7f) //the alpha of the underlying children
        .setHighlightAlpha(0.6f) // the shimmer alpha amount
        .setDirection(Shimmer.Direction.LEFT_TO_RIGHT).setAutoStart(true).build()

    val shimmerDrawable = ShimmerDrawable().apply {
        setShimmer(shimmer)
    }

    fun loadCenterCrop(context: Context, image: String?, listener: ImageListener? = null) {
        Glide.with(context).asBitmap().load(image).placeholder(shimmerDrawable).into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                listener?.onBitmapLoaded(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {

            }
        })
    }

    fun ImageView.load(uri: Uri?) {
        Glide.with(context).load(uri).placeholder(shimmerDrawable).into(this)
    }

    fun ImageView.loadCenterCrop(uri: Uri?) {
        Glide.with(context).load(uri).centerCrop().placeholder(shimmerDrawable)
            .apply(RequestOptions().placeholder(R.drawable.ic_no_image).error(R.drawable.ic_no_image)).into(this)
    }

    fun ImageView.loadUrlCenterCrop(url: String?) {
        Glide.with(context).load(url).centerCrop().placeholder(shimmerDrawable)
            .apply(RequestOptions().placeholder(R.drawable.ic_no_image).error(R.drawable.ic_no_image)).into(this)
    }

    fun ImageView.loadUrlCenterCropNoCache(url: String?) {
        Glide
            .with(context)
            .load(url)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(shimmerDrawable)
            .apply(RequestOptions().placeholder(R.drawable.ic_no_image).error(R.drawable.ic_no_image)).into(this)
    }

    fun ImageView.loadUrl(url: String?) {
        Glide.with(context).load(url).placeholder(shimmerDrawable)
            .apply(RequestOptions().placeholder(R.drawable.ic_no_image).error(R.drawable.ic_no_image)).into(this)
    }

    fun ImageView.loadFileCenterCrop(file: File?) {
        Glide.with(context).load(file).centerCrop().placeholder(shimmerDrawable)
            .apply(RequestOptions().placeholder(R.drawable.ic_no_image).error(R.drawable.ic_no_image)).into(this)
    }

    fun ImageView.loadFile(file: File?) {
        Glide.with(context).load(file).placeholder(shimmerDrawable)
            .apply(RequestOptions().placeholder(R.drawable.ic_no_image).error(R.drawable.ic_no_image)).into(this)
    }

    fun saveImage(context: Context, fileOrigin: File?): Uri? {
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/${AI_GENERATED_PHOTO_FOLDER}")
            values.put(MediaStore.Images.Media.IS_PENDING, true)
            values.put(MediaStore.Images.Media.DISPLAY_NAME, "img_${SystemClock.uptimeMillis()}")

            val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                saveImageToStream(context, fileOrigin, context.contentResolver.openOutputStream(uri))
                values.put(MediaStore.Images.Media.IS_PENDING, false)
                context.contentResolver.update(uri, values, null, null)
                return uri
            }
        } else {
            val directory = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + separator + AI_GENERATED_PHOTO_FOLDER)
            if (!directory.exists()) {
                directory.mkdirs()
            }
            val fileName = "img_${SystemClock.uptimeMillis()}" + ".jpeg"
            val fileNew = File(directory, fileName)

            saveImageToStream(context, fileOrigin, FileOutputStream(fileNew))
            val values = contentValues()
            values.put(MediaStore.Images.Media.DATA, fileNew.absolutePath)
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            return Uri.fromFile(fileNew)
        }
        return null
    }

    private fun contentValues(): ContentValues {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        return values
    }

    fun saveImageToStream(context: Context, file: File?, outputStream: OutputStream?) {
        try {
            val inputStream = context.contentResolver.openInputStream(Uri.fromFile(file))

            if (inputStream != null && outputStream != null) {
                inputStream.copyTo(outputStream, 1024)
            }

            inputStream?.close()
            outputStream?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getBitmap(vectorDrawable: VectorDrawable): Bitmap? {
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return bitmap
    }

    fun getBitmap(context: Context, drawableId: Int): Bitmap? {
        return when (val drawable = ContextCompat.getDrawable(context, drawableId)) {
            is BitmapDrawable -> {
                BitmapFactory.decodeResource(context.resources, drawableId)
            }

            is VectorDrawable -> {
                getBitmap(drawable)
            }

            else -> {
                throw IllegalArgumentException("unsupported drawable type")
            }
        }
    }

    interface ImageListener {
        fun onBitmapLoaded(bitmap: Bitmap) {

        }
    }
}