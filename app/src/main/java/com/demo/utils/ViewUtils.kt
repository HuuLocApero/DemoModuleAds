package com.demo.utils

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Shader
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.text.TextPaint
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import androidx.recyclerview.widget.SnapHelper
import androidx.viewpager.widget.ViewPager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


object ViewUtils {
    fun TextView.setGradiantText(start: String, end: String) {
        val paint: TextPaint = this.paint
        val width: Float = paint.measureText(this.text.toString())

        val textShader: Shader = LinearGradient(
            0f, 0f, width, this.textSize, intArrayOf(
                Color.parseColor(start), Color.parseColor(end)
            ), null, Shader.TileMode.CLAMP
        )
        this.paint.shader = textShader
    }

    fun View.isVisible(): Boolean {
        return this.visibility == View.VISIBLE
    }

    fun View.visible() {
        this.visibility = View.VISIBLE
    }

    fun View.isGone(): Boolean {
        return this.visibility == View.GONE
    }

    fun View.gone() {
        this.visibility = View.GONE
    }

    fun View.isInvisible(): Boolean {
        return this.visibility == View.INVISIBLE
    }

    fun View.invisible() {
        this.visibility = View.INVISIBLE
    }

    fun View.clickWithDebounce(debounceTime: Long = 500L, action: () -> Unit) {
        this.setOnClickListener(object : View.OnClickListener {
            private var lastClickTime: Long = 0
            override fun onClick(v: View) {
                if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
                else action()
                lastClickTime = SystemClock.elapsedRealtime()
            }
        })
    }

    fun View.showView(isShow: Boolean) {
        this.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    fun EditText.textTrim() = this.text.toString().trim()

    fun EditText.intValue() = try {
        this.text.toString().trim().toInt()
    } catch (ex: Exception) {
        0
    }

    fun <T : View> T.width(function: (Int) -> Unit) {
        if (width == 0) viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                function(width)
            }
        })
        else function(width)
    }

    fun ViewPager.autoScroll(interval: Long) {

        val handler = Handler(Looper.getMainLooper())
        var scrollPosition = 0

        val runnable = object : Runnable {

            override fun run() {
                try {
                    val count = adapter?.count ?: 0
                    setCurrentItem(scrollPosition++ % count, true)

                    handler.postDelayed(this, interval)
                } catch (_: java.lang.Exception) {

                }
            }
        }

        addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                scrollPosition = position + 1
            }

            override fun onPageScrollStateChanged(state: Int) {
                // Not necessary
            }

            override fun onPageScrolled(
                position: Int, positionOffset: Float, positionOffsetPixels: Int
            ) {
                // Not necessary
            }
        })

        handler.post(runnable)
    }

    fun RecyclerView.autoScroll() {

        val handler = Handler(Looper.getMainLooper())
        var isRight = true

        val runnable = Runnable {
            try {
                isRight = if (isRight) {
                    val count = adapter?.itemCount ?: 1
                    smoothScrollToPosition(count - 1)
                    false
                } else {
                    smoothScrollToPosition(0)
                    true
                }
            } catch (_: java.lang.Exception) {

            }
        }
        handler.post(runnable)

        val onScrollListener = object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == SCROLL_STATE_IDLE) {
                    handler.post(runnable)
                }
            }
        }
        removeOnScrollListener(onScrollListener)
        addOnScrollListener(onScrollListener)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun ImageView.getMaskBitmap(imageUrl: String? = null, mContent: Int, mMaskedImage: Int) {
        GlobalScope.launch {
            // if you have https image url then use below line
            //val original: Bitmap = BitmapFactory.decodeStream(URL(imageUrl).openConnection().getInputStream())

            // if you have png or jpg image then use below line
            val original: Bitmap = BitmapFactory.decodeResource(resources, mContent)

            val mask = ImageUtils.getBitmap(this@getMaskBitmap.context, mMaskedImage)
            val result: Bitmap = Bitmap.createBitmap(mask!!.width, mask.height, Bitmap.Config.ARGB_8888, true)
            val tempCanvas = Canvas(result)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
            tempCanvas.apply {
                drawBitmap(original, 0f, 0f, null)
                drawBitmap(mask, 0f, 0f, paint)
            }
            paint.xfermode = null

            //Draw result after performing masking
            withContext(Dispatchers.Main) {
                this.apply {
                    setImageBitmap(result)
                    setOnTouchListener { v, event ->
                        val x = event.x.toInt()
                        val y = event.y.toInt()
                        if (drawable.bounds.contains(x, y) && event.action === MotionEvent.ACTION_DOWN) {
                            Log.d("getMaskBitmap", "getMaskBitmap: ")
                            true
                        }
                        false
                    }
                    setOnClickListener {
                        Toast.makeText(this@getMaskBitmap.context, "23123", Toast.LENGTH_LONG).show()
                    }
                    scaleType = ImageView.ScaleType.FIT_CENTER
                }
            }
        }
    }

    fun SnapHelper.getSnapPosition(recyclerView: RecyclerView): Int {
        val layoutManager = recyclerView.layoutManager ?: return RecyclerView.NO_POSITION
        val snapView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
        return layoutManager.getPosition(snapView)
    }
}