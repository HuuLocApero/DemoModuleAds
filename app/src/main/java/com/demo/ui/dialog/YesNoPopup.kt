package com.demo.ui.dialog

import androidx.fragment.app.FragmentManager
import com.demomoduleads.R
import com.demomoduleads.databinding.DialogYesNoBinding
import com.demo.base.BaseDialog
import com.demo.base.BaseViewModel
import com.demo.utils.ViewUtils.clickWithDebounce

class YesNoPopup : BaseDialog<BaseViewModel, DialogYesNoBinding>(
    R.layout.dialog_yes_no, BaseViewModel::class.java
) {

    var title: String = ""
    var content: String = ""
    var acceptText: String? = ""
    var denyText: String? = ""
    var listener: YesNoListener? = null

    companion object {
        fun showPopup(
            fragmentManager: FragmentManager,
            title: String,
            content: String,
            acceptText: String? = null,
            denyText: String? = null,
            listener: YesNoListener? = null
        ) {
            val popup = YesNoPopup()
            popup.title = title
            popup.content = content
            popup.acceptText = acceptText
            popup.denyText = denyText
            popup.listener = listener
            popup.show(fragmentManager, YesNoPopup::class.java.simpleName)
        }
    }

    override fun initData() {
        disableAdsResume()
        with(binding) {
            tvTitle.text = title
            tvContent.text = content
        }
    }

    override fun initListener() {
        with(binding) {
            denyText?.run {
                tvCancel.text = this
            }
            tvCancel.clickWithDebounce {
                enableAdsResume()
                listener?.onCancelPresses()
                dismiss()
            }

            acceptText?.run {
                tvContinue.text = this
            }
            tvContinue.clickWithDebounce {
                enableAdsResume()
                listener?.onContinuePresses()
                dismiss()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        enableAdsResume()
    }

    interface YesNoListener {
        fun onCancelPresses() {

        }

        fun onContinuePresses()
    }
}