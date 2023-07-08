package com.demo.ui.dialog

import androidx.core.util.Consumer
import androidx.fragment.app.FragmentManager
import com.demomoduleads.R
import com.demomoduleads.databinding.DialogNoInternetBinding
import com.demo.base.BaseDialog
import com.demo.base.BaseViewModel
import com.demo.utils.ViewUtils.clickWithDebounce

class NoInternetPopup : BaseDialog<BaseViewModel, DialogNoInternetBinding>(
    R.layout.dialog_no_internet, BaseViewModel::class.java
) {

    var content: String = ""
    var callBack: Consumer<Boolean>? = null

    companion object {
        fun showPopup(fragmentManager: FragmentManager, callBack: Consumer<Boolean>? = null) {
            val popup = NoInternetPopup()
            popup.callBack = callBack
            popup.show(fragmentManager, NoInternetPopup::class.java.simpleName)
        }
    }

    override fun initView() {
        disableAdsResume()
    }

    override fun initListener() {
        binding.btnTryAgain.clickWithDebounce {
            enableAdsResume()
            callBack?.accept(true)
            dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disableAdsResume()
    }
}