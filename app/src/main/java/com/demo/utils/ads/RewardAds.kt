package com.demo.utils.ads

import android.app.Activity
import android.util.Log
import com.ads.control.admob.Admob
import com.ads.control.ads.AperoAd
import com.ads.control.ads.AperoAdCallback
import com.ads.control.ads.wrapper.ApAdError
import com.ads.control.ads.wrapper.ApRewardAd
import com.ads.control.billing.AppPurchase
import com.ads.control.funtion.AdCallback
import com.demo.utils.AppUtils.isNetworkConnected
import com.google.android.gms.ads.LoadAdError
import com.demo.utils.ads.AdsUtils.Status
import com.demo.utils.firebase.FirebaseUtils
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd

class RewardAds(
    val rewardLoaderHigh: RewardLoader? = null,
    val rewardLoaderNormal: RewardLoader? = null,
    val rewardLoaderLow: RewardLoader? = null,
    var adsName: String = "",
    var eventView: String = "",
    var eventClick: String = "",
    var useController: Boolean = false
) {

    init {
        rewardLoaderHigh?.adsName = adsName
        rewardLoaderHigh?.eventView = eventView
        rewardLoaderHigh?.eventClick = eventClick
        rewardLoaderHigh?.useController = useController

        rewardLoaderNormal?.adsName = adsName
        rewardLoaderNormal?.eventView = eventView
        rewardLoaderNormal?.eventClick = eventClick
        rewardLoaderNormal?.useController = useController

        rewardLoaderNormal?.adsName = adsName
        rewardLoaderNormal?.eventView = eventView
        rewardLoaderNormal?.eventClick = eventClick
        rewardLoaderNormal?.useController = useController
    }

    var showAfterClick: Int = 0
    var countShown: Int = 0

    companion object {
        private val TAG = RewardAds::class.java.simpleName


        var lastShowReward: Long = 0L
        private var intervalTime: Long = 0L
        private var isShowByIntervalTime: Boolean = false

        fun setShowByIntervalTime(isShow: Boolean, time: Long = 0) {
            intervalTime = time
            isShowByIntervalTime = isShow
        }
    }

    fun updateAdsName(name: String) {
        this.adsName = name
        rewardLoaderHigh?.adsName = name
        rewardLoaderNormal?.adsName = name
        rewardLoaderLow?.adsName = name
    }

    private fun canShowAds(): Boolean {
        if (rewardLoaderHigh?.canShowAds() == true || rewardLoaderNormal?.canShowAds() == true || rewardLoaderLow?.canShowAds() == true) {
            return true
        }
        return false
    }

    private fun checkShowByIntervalTime(): Boolean {
        return if (isShowByIntervalTime) {
            System.currentTimeMillis() - lastShowReward > intervalTime
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

    fun show(activity: Activity, reloadAfterShow: Boolean = false, nextActionCallBack: (() -> Unit)) {
        val finishCallback: (Boolean) -> Unit = run {
            { isReload ->
                nextActionCallBack()
                if (isReload) {
                    loadAds(activity, null)
                }
                if (useController) {
                    AdsUtils.rewardAll.loadAds(activity)
                }
            }
        }

        AdsUtils.rewardAll.updateAdsName(adsName)
        if (!AppPurchase.getInstance()
                .isPurchased(activity) && (canShowAds() || AdsUtils.rewardAll.canShowAds()) && (!isShowByIntervalTime || checkShowByIntervalTime()) && checkShowBySkip()
        ) {
            // show all first
            FirebaseUtils.eventMustDisplayAds(adsName)
            if (useController && AdsUtils.rewardAll.rewardLoaderHigh?.canShowAds() == true) {
                AdsUtils.rewardAll.rewardLoaderHigh.showAds(activity, object : AperoAdCallback() {
                    override fun onNextAction() {
                        super.onNextAction()
                        finishCallback(true)
                    }

                    override fun onAdFailedToShow(adError: ApAdError?) {
                        super.onAdFailedToShow(adError)
                        countShown = 0
                    }
                })
            } else if (useController && AdsUtils.rewardAll.rewardLoaderNormal?.canShowAds() == true) {
                AdsUtils.rewardAll.rewardLoaderNormal.showAds(activity, object : AperoAdCallback() {
                    override fun onNextAction() {
                        super.onNextAction()
                        finishCallback(true)
                    }

                    override fun onAdFailedToShow(adError: ApAdError?) {
                        super.onAdFailedToShow(adError)
                        countShown = 0
                    }
                })
            } else if (useController && AdsUtils.rewardAll.rewardLoaderLow?.canShowAds() == true) {
                AdsUtils.rewardAll.rewardLoaderLow.showAds(activity, object : AperoAdCallback() {
                    override fun onNextAction() {
                        super.onNextAction()
                        finishCallback(true)
                    }

                    override fun onAdFailedToShow(adError: ApAdError?) {
                        super.onAdFailedToShow(adError)
                        countShown = 0
                    }
                })
            } else if (rewardLoaderHigh?.canShowAds() == true) {
                rewardLoaderHigh.showAds(activity, object : AperoAdCallback() {
                    override fun onNextAction() {
                        super.onNextAction()
                        finishCallback(reloadAfterShow)
                    }

                    override fun onAdFailedToShow(adError: ApAdError?) {
                        super.onAdFailedToShow(adError)
                        countShown = 0
                    }
                })
            } else if (rewardLoaderNormal?.canShowAds() == true) {
                rewardLoaderNormal.showAds(activity, object : AperoAdCallback() {
                    override fun onNextAction() {
                        super.onNextAction()
                        finishCallback(reloadAfterShow)
                    }

                    override fun onAdFailedToShow(adError: ApAdError?) {
                        super.onAdFailedToShow(adError)
                        countShown = 0
                    }
                })
            } else if (rewardLoaderLow?.canShowAds() == true) {
                rewardLoaderLow.showAds(activity, object : AperoAdCallback() {
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
    }

    fun loadAds(activity: Activity, listener: RewardLoader.RewardUtilsListener? = null) {
        rewardLoaderHigh?.loadAds(activity, object : RewardLoader.RewardUtilsListener {

            override fun onAdRewardLoaded(apRewardAd: ApRewardAd) {
                Log.d(TAG, "reward:load success high ${rewardLoaderHigh.idAds}")
                listener?.onAdRewardLoaded(apRewardAd)
            }

            override fun onFailToLoad(error: LoadAdError?) {
                if ((rewardLoaderNormal == null || rewardLoaderNormal.isFail()) && (rewardLoaderLow == null || rewardLoaderLow.isFail())) {
                    listener?.onFailToLoad(error)
                }
            }
        })
        rewardLoaderNormal?.loadAds(activity, object : RewardLoader.RewardUtilsListener {

            override fun onAdRewardLoaded(apRewardAd: ApRewardAd) {
                Log.d(TAG, "reward:load success normal ${rewardLoaderNormal.idAds}")
                if (rewardLoaderHigh == null || rewardLoaderHigh.isFail()) {
                    listener?.onAdRewardLoaded(apRewardAd)
                }
            }

            override fun onFailToLoad(error: LoadAdError?) {
                if ((rewardLoaderHigh == null || rewardLoaderHigh.isFail()) && (rewardLoaderLow == null || rewardLoaderLow.isFail())) {
                    listener?.onFailToLoad(error)
                }
            }
        })
        rewardLoaderLow?.loadAds(activity, object : RewardLoader.RewardUtilsListener {
            override fun onAdRewardLoaded(apRewardAd: ApRewardAd) {
                Log.d(TAG, "reward:load success low ${rewardLoaderLow.idAds}")
                if ((rewardLoaderHigh == null || rewardLoaderHigh.isFail()) && (rewardLoaderNormal == null || rewardLoaderNormal.isFail())) {
                    listener?.onAdRewardLoaded(apRewardAd)
                }
            }

            override fun onFailToLoad(error: LoadAdError?) {
                if ((rewardLoaderHigh == null || rewardLoaderHigh.isFail()) && (rewardLoaderNormal == null || rewardLoaderNormal.isFail())) {
                    listener?.onFailToLoad(error)
                }
            }
        })
    }

    class RewardLoader(
        val idAds: String, val reloadTime: Int = 1, val condition: Boolean = true, private val isRewardInterstitial: Boolean
    ) {
        var adsName: String = ""
        var eventView: String = ""
        var eventClick: String = ""
        var useController: Boolean = false

        private var apRewardAd: ApRewardAd? = null
        private var status: Status = Status.NONE

        private fun isFailOrShownOrNoneAds() = status == Status.FAIL || status == Status.SHOWN || status == Status.NONE

        fun isLoadSuccess() = status == Status.SUCCESS
        private fun isLoading() = status == Status.LOADING

        fun isFail() = status == Status.FAIL

        fun canShowAds(): Boolean {
            if (condition && apRewardAd != null) {
                return true
            }
            return false
        }

        fun showAds(activity: Activity, listener: AperoAdCallback? = null) {
            if (canShowAds() && condition) {
                FirebaseUtils.eventDisplayAds(adsName, idAds)
                AperoAd.getInstance()?.forceShowRewardAd(activity, apRewardAd!!, object : AperoAdCallback() {
                    override fun onAdFailedToShow(adError: ApAdError?) {
                        super.onAdFailedToShow(adError)
                        Log.d(TAG, "reward:showAds fail $idAds")
                        listener?.onAdFailedToShow(adError)
                    }

                    override fun onNextAction() {
                        super.onNextAction()
                        lastShowReward = System.currentTimeMillis()
                        status = Status.SHOWN
                        Log.d(TAG, "reward:showAds success $idAds")
                        listener?.onNextAction()
                    }


                    override fun onAdImpression() {
                        super.onAdImpression()
                        FirebaseUtils.trackEvent(eventView, "ad_format", "reward")
                        FirebaseUtils.eventImpressionAds(adsName, idAds)
                        Log.d(TAG, "reward :onAdImpression $idAds")
                    }

                    override fun onAdClicked() {
                        super.onAdClicked()
                        FirebaseUtils.trackEvent(eventClick, "ad_format", "reward")
                    }
                })
            } else {
                listener?.onAdFailedToShow(ApAdError("Fail"))
            }
        }

        fun loadAds(activity: Activity, listener: RewardUtilsListener? = null) {
            if (condition && isFailOrShownOrNoneAds() && isNetworkConnected(activity)) {
                if (isRewardInterstitial) {
                    loadInterstitialAds(activity, reloadTime, listener)
                } else {
                    loadAdsReward(activity, reloadTime, listener)
                }
            }
        }

        private fun loadAdsReward(activity: Activity, timeReload: Int, listener: RewardUtilsListener? = null) {
            if (timeReload > 0) {
                status = Status.LOADING
                val adRequest = AdRequest.Builder().build()
                RewardedAd.load(activity, idAds, adRequest, object : RewardedAdLoadCallback() {

                    override fun onAdLoaded(rewardedAd: RewardedAd) {
                        super.onAdLoaded(rewardedAd)
                        Log.d(TAG, "onAdLoaded: reward $idAds")
                        apRewardAd = ApRewardAd()
                        apRewardAd?.admobReward = rewardedAd
                        status = Status.SUCCESS

                        Log.d(TAG, "reward: load SUCCESS times(${reloadTime - timeReload + 1}) $idAds")

                        listener?.onAdRewardLoaded(apRewardAd!!)
                    }

                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        super.onAdFailedToLoad(adError)
                        Log.d(TAG, "reward: fail times(${reloadTime - timeReload + 1}) id(${idAds})")
                        if (timeReload - 1 > 0) {
                            loadAdsReward(activity, timeReload - 1, listener)
                        } else {
                            status = Status.FAIL
                            listener?.onFailToLoad(adError)
                        }
                    }
                })
            }
        }

        private fun loadInterstitialAds(activity: Activity, timeReload: Int, listener: RewardUtilsListener? = null) {
            if (timeReload > 0) {
                status = Status.LOADING
                Admob.getInstance()?.getRewardInterstitial(activity, idAds, object : AdCallback() {

                    override fun onRewardAdLoaded(rewardedAd: RewardedInterstitialAd?) {
                        super.onRewardAdLoaded(rewardedAd)
                        Log.d(TAG, "onAdLoaded: inter reward $idAds")
                        apRewardAd = ApRewardAd()
                        apRewardAd?.setAdmobReward(rewardedAd)
                        status = Status.SUCCESS

                        Log.d(TAG, "reward: load SUCCESS times(${reloadTime - timeReload + 1}) $idAds")

                        listener?.onAdRewardLoaded(apRewardAd!!)
                    }

                    override fun onAdFailedToLoad(adError: LoadAdError?) {
                        super.onAdFailedToLoad(adError)
                        Log.d(TAG, "reward: fail times(${reloadTime - timeReload + 1}) id(${idAds})")
                        if (timeReload - 1 > 0) {
                            loadInterstitialAds(activity, timeReload - 1, listener)
                        } else {
                            status = Status.FAIL
                            listener?.onFailToLoad(adError)
                        }
                    }
                })
            }
        }

        interface RewardUtilsListener {

            fun onAdRewardLoaded(apRewardAd: ApRewardAd)

            fun onFailToLoad(error: LoadAdError?)
        }
    }
}
