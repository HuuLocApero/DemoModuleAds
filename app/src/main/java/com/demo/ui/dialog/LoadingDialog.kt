package com.demo.ui.dialog

import com.demomoduleads.R
import com.demomoduleads.databinding.DialogLoadingBinding
import com.demo.base.BaseDialog
import com.demo.base.BaseViewModel

class LoadingDialog : BaseDialog<BaseViewModel, DialogLoadingBinding>(
    R.layout.dialog_loading, BaseViewModel::class.java
) {
    override fun initData() {
        disableAdsResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        enableAdsResume()
    }
}