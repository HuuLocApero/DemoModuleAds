package com.demo.utils.ads

import android.content.Context
import android.util.Log
import com.ads.control.ads.AperoAd
import com.ads.control.ads.AperoAdCallback
import com.ads.control.ads.wrapper.ApAdError
import com.ads.control.ads.wrapper.ApInterstitialAd
import com.demo.utils.AppUtils
import com.demo.utils.firebase.FirebaseUtils

class InterstitialAds(
    val interLoaderHigh: InterstitialLoader? = null,
    val interLoaderNormal: InterstitialLoader? = null,
    val interLoaderLow: InterstitialLoader? = null,
    var adsName: String = "",
    var eventView: String = "",
    var eventClick: String = "",
    var useController: Boolean = false
) {

    init {
        interLoaderHigh?.adsName = adsName
        interLoaderHigh?.eventView = eventView
        interLoaderHigh?.eventClick = eventClick
        interLoaderHigh?.useController = useController

        interLoaderNormal?.adsName = adsName
        interLoaderNormal?.eventView = eventView
        interLoaderNormal?.eventClick = eventClick
        interLoaderNormal?.useController = useController

        interLoaderLow?.adsName = adsName
        interLoaderLow?.eventView = eventView
        interLoaderLow?.eventClick = eventClick
        interLoaderLow?.useController = useController
    }

    var showAfterClick: Int = 0
    var countShown: Int = 0

    companion object {
        private val TAG = InterstitialAds::class.java.simpleName


        var lastShowInter: Long = 0L
        private var intervalTime: Long = 0L
        private var isShowByIntervalTime: Boolean = false

        fun setShowByIntervalTime(isShow: Boolean, time: Long = 0) {
            intervalTime = time
            isShowByIntervalTime = isShow
        }
    }

    fun updateAdsName(name: String, eventView: String, eventClick: String) {
        this.adsName = name
        this.eventView = eventView
        this.eventClick = eventClick

        interLoaderHigh?.adsName = name
        interLoaderHigh?.eventView = eventView
        interLoaderHigh?.eventClick = eventClick

        interLoaderNormal?.adsName = name
        interLoaderNormal?.eventView = eventView
        interLoaderNormal?.eventClick = eventClick

        interLoaderLow?.adsName = name
        interLoaderLow?.eventView = eventView
        interLoaderLow?.eventClick = eventClick
    }

    fun clearAds() {
        interLoaderHigh?.interstitialAd = null
        interLoaderHigh?.status = AdsUtils.Status.NONE

        interLoaderNormal?.interstitialAd = null
        interLoaderNormal?.status = AdsUtils.Status.NONE

        interLoaderLow?.interstitialAd = null
        interLoaderLow?.status = AdsUtils.Status.NONE
    }

    private fun canShowAds(): Boolean {
        if (interLoaderHigh?.canShowAds() == true || interLoaderNormal?.canShowAds() == true || interLoaderLow?.canShowAds() == true) {
            return true
        }
        return false
    }

    private fun checkShowByIntervalTime(): Boolean {
        return if (isShowByIntervalTime) {
            System.currentTimeMillis() - lastShowInter > intervalTime
        } else {
            true
        }
    }

    private fun checkShowBySkip(): Boolean {
        if (showAfterClick == 0) {
            return true
        }
        if (countShown < 0) {
            countShown++
            return false
        }
        if (countShown == 0 || countShown % (showAfterClick + 1) == 0) {
            countShown++
            return true
        }
        countShown++
        return false
    }

    fun allowShowAds() = interLoaderHigh?.condition == true || interLoaderNormal?.condition == true || interLoaderLow?.condition == true


    fun show(context: Context, reloadAfterShow: Boolean = false, nextActionCallBack: (() -> Unit)) {
        val finishCallback: (Boolean) -> Unit = run {
            { isReload ->
                nextActionCallBack()
                if (isReload) {
                    loadAds(context, null)
                }
                if (useController) {
                    AdsUtils.interAll.loadAds(context)
                }
            }
        }

        AdsUtils.interAll.updateAdsName(adsName, eventView, eventClick)
        if (allowShowAds()) {
            if ((canShowAds() || AdsUtils.interAll.canShowAds()) && (!isShowByIntervalTime || checkShowByIntervalTime()) && checkShowBySkip()) {
                FirebaseUtils.eventMustDisplayAds(adsName)
                // show all first
                if (useController && AdsUtils.interAll.interLoaderHigh?.canShowAds() == true) {
                    AdsUtils.interAll.interLoaderHigh.showAds(context, object : AperoAdCallback() {
                        override fun onNextAction() {
                            super.onNextAction()
                            finishCallback(true)
                        }

                        override fun onAdFailedToShow(adError: ApAdError?) {
                            super.onAdFailedToShow(adError)
                            countShown = 0
                        }
                    })
                } else if (useController && AdsUtils.interAll.interLoaderNormal?.canShowAds() == true) {
                    AdsUtils.interAll.interLoaderNormal.showAds(context, object : AperoAdCallback() {
                        override fun onNextAction() {
                            super.onNextAction()
                            finishCallback(true)
                        }

                        override fun onAdFailedToShow(adError: ApAdError?) {
                            super.onAdFailedToShow(adError)
                            countShown = 0
                        }
                    })
                } else if (useController && AdsUtils.interAll.interLoaderLow?.canShowAds() == true) {
                    AdsUtils.interAll.interLoaderLow.showAds(context, object : AperoAdCallback() {
                        override fun onNextAction() {
                            super.onNextAction()
                            finishCallback(true)
                        }

                        override fun onAdFailedToShow(adError: ApAdError?) {
                            super.onAdFailedToShow(adError)
                            countShown = 0
                        }
                    })
                } else if (interLoaderHigh?.canShowAds() == true) {
                    interLoaderHigh.showAds(context, object : AperoAdCallback() {
                        override fun onNextAction() {
                            super.onNextAction()
                            finishCallback(reloadAfterShow)
                        }

                        override fun onAdFailedToShow(adError: ApAdError?) {
                            super.onAdFailedToShow(adError)
                            countShown = 0
                        }
                    })
                } else if (interLoaderNormal?.canShowAds() == true) {
                    interLoaderNormal.showAds(context, object : AperoAdCallback() {
                        override fun onNextAction() {
                            super.onNextAction()
                            finishCallback(reloadAfterShow)
                        }

                        override fun onAdFailedToShow(adError: ApAdError?) {
                            super.onAdFailedToShow(adError)
                            countShown = 0
                        }
                    })
                } else if (interLoaderLow?.canShowAds() == true) {
                    interLoaderLow.showAds(context, object : AperoAdCallback() {
                        override fun onNextAction() {
                            super.onNextAction()
                            finishCallback(reloadAfterShow)
                        }

                        override fun onAdFailedToShow(adError: ApAdError?) {
                            super.onAdFailedToShow(adError)
                            countShown = 0
                        }
                    })
                } else {
                    finishCallback(reloadAfterShow)
                }
            } else {
                finishCallback(reloadAfterShow)
            }
        } else {
            finishCallback(false)
        }
    }

    fun loadAds(context: Context, listener: AperoAdCallback? = null) {
        interLoaderHigh?.loadAds(context, object : AperoAdCallback() {
            override fun onInterstitialLoad(interstitialAd: ApInterstitialAd?) {
                super.onInterstitialLoad(interstitialAd)
                Log.d(TAG, "inter:load success high ${interLoaderHigh.idAds}")
                listener?.onInterstitialLoad(interstitialAd)
            }

            override fun onAdFailedToLoad(adError: ApAdError?) {
                super.onAdFailedToLoad(adError)
                if ((interLoaderNormal == null || interLoaderNormal.isFail()) && (interLoaderLow == null || interLoaderLow.isFail())) {
                    listener?.onAdFailedToLoad(adError)
                }
            }
        })
        interLoaderNormal?.loadAds(context, object : AperoAdCallback() {
            override fun onInterstitialLoad(interstitialAd: ApInterstitialAd?) {
                super.onInterstitialLoad(interstitialAd)
                Log.d(TAG, "inter:load success normal ${interLoaderNormal.idAds}")
                if (interLoaderHigh == null || interLoaderHigh.isFail()) {
                    listener?.onInterstitialLoad(interstitialAd)
                }
            }

            override fun onAdFailedToLoad(adError: ApAdError?) {
                super.onAdFailedToLoad(adError)
                if ((interLoaderHigh == null || interLoaderHigh.isFail()) && (interLoaderLow == null || interLoaderLow.isFail())) {
                    listener?.onAdFailedToLoad(adError)
                }
            }
        })
        interLoaderLow?.loadAds(context, object : AperoAdCallback() {
            override fun onInterstitialLoad(interstitialAd: ApInterstitialAd?) {
                super.onInterstitialLoad(interstitialAd)
                Log.d(TAG, "inter:load success low ${interLoaderLow.idAds}")
                if ((interLoaderHigh == null || interLoaderHigh.isFail()) && (interLoaderNormal == null || interLoaderNormal.isFail())) {
                    listener?.onInterstitialLoad(interstitialAd)
                }
            }

            override fun onAdFailedToLoad(adError: ApAdError?) {
                super.onAdFailedToLoad(adError)
                if ((interLoaderHigh == null || interLoaderHigh.isFail()) && (interLoaderNormal == null || interLoaderNormal.isFail())) {
                    listener?.onAdFailedToLoad(adError)
                }
            }
        })
    }

    class InterstitialLoader(
        val idAds: String, val reloadTime: Int = 1, var condition: Boolean = true
    ) {
        var adsName: String = ""
        var eventView: String = ""
        var eventClick: String = ""
        var useController: Boolean = false

        var interstitialAd: ApInterstitialAd? = null
        var status: AdsUtils.Status = AdsUtils.Status.NONE

        private fun isFailOrShownOrNoneAds() = status == AdsUtils.Status.FAIL || status == AdsUtils.Status.SHOWN || status == AdsUtils.Status.NONE

        fun isLoadSuccess() = status == AdsUtils.Status.SUCCESS

        fun isFail() = status == AdsUtils.Status.FAIL

        fun canShowAds(): Boolean {
            if (condition && interstitialAd != null) {
                return true
            }
            return false
        }

        fun showAds(context: Context, listener: AperoAdCallback? = null) {
            if (canShowAds() && condition) {
                FirebaseUtils.eventDisplayAds(adsName, idAds)
                AperoAd.getInstance().forceShowInterstitial(
                    context, interstitialAd, object : AperoAdCallback() {
                        override fun onNextAction() {
                            super.onNextAction()
                            lastShowInter = System.currentTimeMillis()
                            status = AdsUtils.Status.SHOWN
                            Log.d(TAG, "inter:showAds success $idAds")
                            listener?.onNextAction()
                        }

                        override fun onAdFailedToShow(adError: ApAdError?) {
                            super.onAdFailedToShow(adError)
                            Log.d(TAG, "inter:showAds fail $idAds")
                            listener?.onAdFailedToShow(adError)
                        }

                        override fun onAdImpression() {
                            super.onAdImpression()
                            FirebaseUtils.trackEvent(eventView, "ad_format", "interstitial")
                            FirebaseUtils.eventImpressionAds(adsName, idAds)
                            Log.d(TAG, "inter:onAdImpression $idAds")
                        }

                        override fun onAdClicked() {
                            super.onAdClicked()
                            FirebaseUtils.trackEvent(eventClick, "ad_format", "interstitial")
                        }
                    }, false
                )
            } else {
                listener?.onAdFailedToShow(ApAdError("Fail"))
            }
        }

        fun loadAds(context: Context?, listener: AperoAdCallback? = null) {
            if (condition && isFailOrShownOrNoneAds() && AppUtils.isNetworkConnected(context)) {
                loadAdsReloadIfFail(context, reloadTime, listener)
            }
        }

        private fun loadAdsReloadIfFail(
            context: Context?, timeReload: Int, listener: AperoAdCallback?
        ) {
            if (timeReload > 0) {
                status = AdsUtils.Status.LOADING
                AperoAd.getInstance().getInterstitialAds(context, idAds, object : AperoAdCallback() {
                    override fun onInterstitialLoad(interstitialAd: ApInterstitialAd?) {
                        super.onInterstitialLoad(interstitialAd)
                        Log.d(
                            TAG, "inter: load SUCCESS times(${reloadTime - timeReload + 1}) $idAds"
                        )
                        this@InterstitialLoader.interstitialAd = interstitialAd
                        listener?.onInterstitialLoad(interstitialAd)
                        status = AdsUtils.Status.SUCCESS
                    }

                    override fun onAdFailedToLoad(adError: ApAdError?) {
                        super.onAdFailedToLoad(adError)
                        Log.d(
                            TAG, "inter: fail times(${reloadTime - timeReload + 1}) id(${idAds})"
                        )
                        if (timeReload - 1 > 0) {
                            loadAdsReloadIfFail(context, timeReload - 1, listener)
                        } else {
                            status = AdsUtils.Status.FAIL
                            listener?.onAdFailedToLoad(adError)
                        }
                    }
                })
            }
        }

    }
}