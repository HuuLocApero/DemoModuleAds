package com.demo.utils.ads

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.ads.control.admob.Admob
import com.ads.control.ads.AperoAd
import com.ads.control.ads.AperoAdCallback
import com.ads.control.ads.wrapper.ApAdError
import com.ads.control.ads.wrapper.ApNativeAd
import com.demo.utils.AppUtils
import com.demo.utils.firebase.FirebaseUtils
import com.google.android.gms.ads.nativead.NativeAdView

class NativeAds(
    var layoutCustom: Int,
    val nativeLoaderHigh: NativeLoader? = null,
    val nativeLoaderNormal: NativeLoader? = null,
    val nativeLoaderLow: NativeLoader? = null,
    var adsName: String = "",
    var eventView: String = "",
    var eventClick: String = "",
    var useController: Boolean = false
) {
    init {
        nativeLoaderHigh?.layoutCustom = layoutCustom
        nativeLoaderHigh?.adsName = adsName
        nativeLoaderHigh?.eventView = eventView
        nativeLoaderHigh?.eventClick = eventClick
        nativeLoaderHigh?.useController = useController

        nativeLoaderNormal?.layoutCustom = layoutCustom
        nativeLoaderNormal?.adsName = adsName
        nativeLoaderNormal?.eventView = eventView
        nativeLoaderNormal?.eventClick = eventClick
        nativeLoaderNormal?.useController = useController

        nativeLoaderLow?.layoutCustom = layoutCustom
        nativeLoaderLow?.adsName = adsName
        nativeLoaderLow?.eventView = eventView
        nativeLoaderLow?.eventClick = eventClick
        nativeLoaderLow?.useController = useController
    }

    companion object {
        private val TAG = NativeAds::class.java.name
    }

    private var isReadLoadAfterShow: Boolean = true
    fun hasAvailableAds() =
        nativeLoaderHigh?.canShowAds() == true || nativeLoaderNormal?.canShowAds() == true || nativeLoaderLow?.canShowAds() == true

    fun clearAds() {
        nativeLoaderHigh?.nativeAd = null
        nativeLoaderHigh?.status = AdsUtils.Status.NONE

        nativeLoaderNormal?.nativeAd = null
        nativeLoaderNormal?.status = AdsUtils.Status.NONE

        nativeLoaderLow?.nativeAd = null
        nativeLoaderLow?.status = AdsUtils.Status.NONE
    }

    private fun updateAdsName(name: String, eventView: String, eventClick: String) {
        this.adsName = name
        this.eventView = eventView
        this.eventClick = eventClick
        nativeLoaderHigh?.adsName = name
        nativeLoaderHigh?.eventClick = eventClick
        nativeLoaderHigh?.eventView = eventView

        nativeLoaderNormal?.adsName = name
        nativeLoaderNormal?.eventClick = eventClick
        nativeLoaderNormal?.eventView = eventView

        nativeLoaderLow?.adsName = name
        nativeLoaderLow?.eventClick = eventClick
        nativeLoaderLow?.eventView = eventView
    }

    private fun updateLayout(layout: Int) {
        nativeLoaderHigh?.layoutCustom = layout
        nativeLoaderNormal?.layoutCustom = layout
        nativeLoaderLow?.layoutCustom = layout
    }

    private fun allowShowAds() = nativeLoaderHigh?.condition == true || nativeLoaderNormal?.condition == true || nativeLoaderLow?.condition == true

    fun showAds(activity: Activity, container: FrameLayout, reloadAfterShow: Boolean = true) {
        this.isReadLoadAfterShow = reloadAfterShow
        AdsUtils.nativeAll.updateAdsName(adsName, eventView, eventClick)
        AdsUtils.nativeAll.updateLayout(layoutCustom)
        if (allowShowAds()) {
            FirebaseUtils.eventMustDisplayAds(adsName)
            if (useController && AdsUtils.nativeAll.nativeLoaderHigh?.canShowAds() == true) {
                Log.d(TAG, "showAds: high id(${AdsUtils.nativeAll.nativeLoaderHigh.idAds})")
                AdsUtils.nativeAll.nativeLoaderHigh.showAds(activity, container, false)
            } else if (useController && AdsUtils.nativeAll.nativeLoaderNormal?.canShowAds() == true) {
                Log.d(TAG, "showAds: normal id(${AdsUtils.nativeAll.nativeLoaderNormal.idAds})")
                AdsUtils.nativeAll.nativeLoaderNormal.showAds(activity, container, false)
            } else if (useController && AdsUtils.nativeAll.nativeLoaderLow?.canShowAds() == true) {
                Log.d(TAG, "showAds: low id(${AdsUtils.nativeAll.nativeLoaderLow.idAds})")
                AdsUtils.nativeAll.nativeLoaderLow.showAds(activity, container, false)
            } else if (nativeLoaderHigh?.canShowAds() == true) {
                Log.d(TAG, "showAds: high id(${nativeLoaderHigh.idAds})")
                nativeLoaderHigh.showAds(activity, container, reloadAfterShow)
            } else if (nativeLoaderNormal?.canShowAds() == true) {
                Log.d(TAG, "showAds: normal id(${nativeLoaderNormal.idAds})")
                nativeLoaderNormal.showAds(activity, container, reloadAfterShow)
            } else if (nativeLoaderLow?.canShowAds() == true) {
                Log.d(TAG, "showAds: low id(${nativeLoaderLow.idAds})")
                nativeLoaderLow.showAds(activity, container, reloadAfterShow)
            } else {
                if (AppUtils.isNetworkConnected(activity) && allowShowAds()) {
                    loadAds(activity, object : NativeLoaderListener() {
                        override fun onNativeFloorLoaded(nativeLoader: NativeLoader) {
                            super.onNativeFloorLoaded(nativeLoader)
                            Log.d(TAG, "showAds: id(${nativeLoader.idAds})")
                            nativeLoader.showAds(activity, container)
                        }

                        override fun onAdFailedToLoad(adError: ApAdError?) {
                            super.onAdFailedToLoad(adError)
                            Log.d(TAG, "showAds: Fail $adsName (${adError?.message})")
                            container.visibility = View.GONE
                        }
                    })
                } else {
                    container.visibility = View.GONE
                }
            }
        } else {
            container.visibility = View.GONE
        }
    }

    fun loadAds(activity: Activity, listener: NativeLoaderListener? = null) {
        nativeLoaderHigh?.loadAds(activity, object : AperoAdCallback() {
            override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
                super.onNativeAdLoaded(nativeAd)
                listener?.onNativeFloorLoaded(nativeLoaderHigh)
            }

            override fun onAdFailedToLoad(adError: ApAdError?) {
                super.onAdFailedToLoad(adError)
                if ((nativeLoaderNormal == null || nativeLoaderNormal.isFail()) && (nativeLoaderLow == null || nativeLoaderLow.isFail())) {
                    listener?.onAdFailedToLoad(adError)
                }
            }
        })
        nativeLoaderNormal?.loadAds(activity, object : AperoAdCallback() {
            override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
                super.onNativeAdLoaded(nativeAd)
                if (nativeLoaderHigh == null || nativeLoaderHigh.isFail()) {
                    listener?.onNativeFloorLoaded(nativeLoaderNormal)
                }
            }

            override fun onAdFailedToLoad(adError: ApAdError?) {
                super.onAdFailedToLoad(adError)
                if ((nativeLoaderHigh == null || nativeLoaderHigh.isFail()) && (nativeLoaderLow == null || nativeLoaderLow.isFail())) {
                    listener?.onAdFailedToLoad(adError)
                }
            }
        })
        nativeLoaderLow?.loadAds(activity, object : AperoAdCallback() {
            override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
                super.onNativeAdLoaded(nativeAd)
                if ((nativeLoaderHigh == null || nativeLoaderHigh.isFail()) && (nativeLoaderNormal == null || nativeLoaderNormal.isFail())) {
                    listener?.onNativeFloorLoaded(nativeLoaderLow)
                }
            }

            override fun onAdFailedToLoad(adError: ApAdError?) {
                super.onAdFailedToLoad(adError)
                if ((nativeLoaderHigh == null || nativeLoaderHigh.isFail()) && (nativeLoaderNormal == null || nativeLoaderNormal.isFail())) {
                    listener?.onAdFailedToLoad(adError)
                }
            }
        })
    }

    class NativeLoader(val idAds: String, private val reloadTime: Int, var condition: Boolean = true) {
        var layoutCustom: Int = 0
        var nativeAd: ApNativeAd? = null
        var status: AdsUtils.Status = AdsUtils.Status.NONE

        var adsName: String = ""
        var eventView: String = ""
        var eventClick: String = ""
        var useController: Boolean = false
        private fun isFailOrShownOrNoneAds() = status == AdsUtils.Status.FAIL || status == AdsUtils.Status.SHOWN || status == AdsUtils.Status.NONE

        fun isLoadSuccess() = status == AdsUtils.Status.SUCCESS
        fun isFail() = status == AdsUtils.Status.FAIL

        private fun isLoading() = status == AdsUtils.Status.LOADING

        fun canShowAds(): Boolean {
            if (condition && nativeAd != null && !isFailOrShownOrNoneAds() && !isLoading()) {
                return true
            }
            return false
        }

        fun showAds(activity: Activity?, container: FrameLayout, isReload: Boolean = true) {
            if (canShowAds()) {
                FirebaseUtils.eventDisplayAds(adsName, idAds)
                populateNativeAdView(activity, container)
                if (isReload) {
                    loadAds(activity)
                }
                if (useController && activity != null) {
                    AdsUtils.nativeAll.loadAds(activity)
                }
            }
        }

        private fun populateNativeAdView(activity: Activity?, container: FrameLayout) {
            if (activity != null && canShowAds()) {
                try {
                    val adView = LayoutInflater.from(activity).inflate(layoutCustom, null) as NativeAdView
                    Admob.getInstance().populateUnifiedNativeAdView(nativeAd!!.admobNativeAd, adView)
                    container.removeAllViews()
                    container.post {
                        container.requestFocus()
                        container.addView(adView)
                    }
                    status = AdsUtils.Status.SHOWN
                    Log.d(TAG, "populateNativeAdView: success id(${idAds})")
                } catch (ex: Exception) {
                    Log.d(TAG, "populateNativeAdView: fail id(${idAds})")
                    status = AdsUtils.Status.FAIL
                }
            } else {
                Log.d(TAG, "NativeAds populateNativeAdView: Show fail")
            }
        }

        fun loadAds(activity: Activity?, listener: AperoAdCallback? = null) {
            if (AppUtils.isNetworkConnected(activity) && condition && isFailOrShownOrNoneAds() && !isLoading()) {
                loadNativeReloadIfFail(activity, reloadTime, listener)
            } else {
                listener?.onAdFailedToLoad(ApAdError("no need to load"))
            }
        }

        private fun loadNativeReloadIfFail(activity: Activity?, timeReload: Int, listener: AperoAdCallback?) {
            if (timeReload > 0) {
                status = AdsUtils.Status.LOADING
                AperoAd.getInstance().loadNativeAdResultCallback(activity, idAds, layoutCustom, object : AperoAdCallback() {
                    override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
                        super.onNativeAdLoaded(nativeAd)
                        this@NativeLoader.nativeAd?.admobNativeAd?.destroy()
                        this@NativeLoader.nativeAd = null
                        this@NativeLoader.nativeAd = nativeAd
                        status = AdsUtils.Status.SUCCESS
                        Log.d(
                            TAG, "loadNative: success times(${reloadTime - timeReload + 1}) id(${idAds})"
                        )
                        listener?.onNativeAdLoaded(nativeAd)
                    }

                    override fun onAdFailedToLoad(adError: ApAdError?) {
                        super.onAdFailedToLoad(adError)
                        Log.d(TAG, "loadNative: fail times(${reloadTime - timeReload + 1}) id(${idAds})")
                        if (timeReload - 1 > 0) {
                            loadNativeReloadIfFail(activity, timeReload - 1, listener)
                        } else {
                            status = AdsUtils.Status.FAIL
                            listener?.onAdFailedToLoad(adError)
                        }
                    }

                    override fun onAdImpression() {
                        super.onAdImpression()
                        FirebaseUtils.trackEvent(eventView, "ad_format", "native")
                        FirebaseUtils.eventImpressionAds(adsName, idAds)
                        Log.d(TAG, "onAdImpression id(${idAds})")
                    }

                    override fun onAdClicked() {
                        super.onAdClicked()
                        FirebaseUtils.trackEvent(eventClick, "ad_format", "native")
                    }
                })
            }
        }
    }

    open class NativeLoaderListener : AperoAdCallback() {
        open fun onNativeFloorLoaded(nativeLoader: NativeLoader) {

        }
    }
}