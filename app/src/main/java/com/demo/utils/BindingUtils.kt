package com.demo.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.demo.utils.ImageUtils.loadFile
import com.demo.utils.customview.HeaderView
import java.io.File

@BindingAdapter("headerListener")
fun HeaderView.headerListener(listener: HeaderView.Listener?) {
    this.listener = listener
}

@BindingAdapter("loadFileStr")
fun ImageView.loadFileStr(str: String?) {
    this.loadFile(File(str ?: ""))
}