package com.demo.ui.dialog

import com.demomoduleads.R
import com.demomoduleads.databinding.DialogExitBinding
import com.demo.base.BaseDialog
import com.demo.base.BaseViewModel
import com.demo.utils.ViewUtils.clickWithDebounce

class PopupExit : BaseDialog<BaseViewModel, DialogExitBinding>(
    R.layout.dialog_exit,
    BaseViewModel::class.java
) {

    override fun initView() {
        disableAdsResume()
    }

    override fun initListener() {
        with(binding) {
            tvExit.clickWithDebounce {
                requireActivity().finishAffinity()
            }

            tvCancel.clickWithDebounce {
                dismiss()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        enableAdsResume()
    }
}