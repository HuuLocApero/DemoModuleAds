package com.demo.utils.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.demomoduleads.R
import com.demomoduleads.databinding.LayoutHeaderViewBinding
import com.demo.utils.ViewUtils.clickWithDebounce

class HeaderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    var binding: LayoutHeaderViewBinding

    private var isShowBack = false
    private var isGoneBack = false
    private var isShowOption = false
    private var isGoneOption = false
    private var titleGravity = 17
    private var titleRes = 0
    private var titleTextSize = 0f

    private var optionRes = 0
    private var backRes = 0

    var listener: Listener? = null

    init {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.layout_header_view, null, false
        )
        addView(binding.root)

        context.theme.obtainStyledAttributes(attrs, R.styleable.HeaderView, 0, 0).run {
            isShowBack = getBoolean(R.styleable.HeaderView_showBackButton, false)
            isGoneBack = getBoolean(R.styleable.HeaderView_goneButton, false)
            isGoneBack = getBoolean(R.styleable.HeaderView_goneButton, false)
            isShowOption = getBoolean(R.styleable.HeaderView_showOptionButton, false)
            isGoneOption = getBoolean(R.styleable.HeaderView_goneOptionButton, false)
            titleRes = getResourceId(R.styleable.HeaderView_headerTitle, 0)
            optionRes = getResourceId(R.styleable.HeaderView_optionIcon, 0)
            backRes = getResourceId(R.styleable.HeaderView_backIcon, 0)
            titleGravity = getInt(R.styleable.HeaderView_gravity, 17)
            titleTextSize = getDimension(R.styleable.HeaderView_textSize, 0f)
        }

        with(binding) {
            showBackButton(isShowBack)
            showOptionButton(isShowOption)
            if (titleRes != 0) {
                tvHeader.setText(titleRes)
                tvHeader.gravity = titleGravity
                if (titleTextSize != 0f) {
                    tvHeader.textSize = titleTextSize
                }
            }

            if (optionRes != 0) {
                btnOption.setImageResource(optionRes)
            }

            if (backRes != 0) {
                btnBack.setImageResource(backRes)
            }

            btnBack.clickWithDebounce {
                listener?.onHeaderBackPressed()
            }

            btnOption.clickWithDebounce {
                listener?.onOptionPressed(btnOption)
            }
        }
    }

    fun headerText(title: String) {
        binding.tvHeader.text = title
        invalidate()
    }

    fun showBackButton(isShow: Boolean) {
        binding.btnBack.visibility = if (isShow) View.VISIBLE else (if (isGoneBack) View.GONE else View.INVISIBLE)
    }

    fun showOptionButton(isShow: Boolean) {
        binding.btnOption.visibility = if (isShow) View.VISIBLE else (if (isGoneOption) View.GONE else View.INVISIBLE)
    }

    interface Listener {
        fun onHeaderBackPressed() {

        }

        fun onOptionPressed(view: View) {

        }
    }
}