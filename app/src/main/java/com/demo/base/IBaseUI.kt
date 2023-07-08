package com.demo.base

import android.view.View
import com.demo.utils.firebase.EventClick

interface IBaseUI {

    fun showLoading(content: String)

    fun hideLoading()

    fun isNetworkConnected(): Boolean

    fun hideSystemNavigationBar()

    fun showToast(content: String)

    fun hideKeyboard(view: View?)

    fun checkPermission(permission: String): Boolean

    fun disableAdsResume()

    fun disableAdsResumeByClickAction()

    fun enableAdsResume()

    fun trackEvent(event: EventClick, param: String? = null, value: String? = null)
}