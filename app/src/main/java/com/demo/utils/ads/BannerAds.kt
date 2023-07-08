package com.demo.utils.ads

import android.app.Activity
import android.graphics.Insets
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.widget.FrameLayout
import com.ads.control.ads.AperoAd
import com.ads.control.funtion.AdCallback
import com.demomoduleads.R
import com.demo.utils.AppUtils
import com.demo.utils.firebase.FirebaseUtils
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

class BannerAds(
    bannerDataHigh: BannerData? = null, bannerDataNormal: BannerData? = null, bannerDataLow: BannerData? = null, val adsName: String = ""
) {
    data class BannerData(val adsId: String, val reloadTime: Int, var condition: Boolean)

    var bannerHigh: BannerLoader? = null
    var bannerNormal: BannerLoader? = null
    var bannerLow: BannerLoader? = null

    init {
        bannerDataHigh?.let {
            bannerHigh = BannerLoader(it.adsId, it.reloadTime, it.condition)
            bannerHigh?.adsName = adsName
        }
        bannerDataNormal?.let {
            bannerNormal = BannerLoader(it.adsId, it.reloadTime, it.condition)
            bannerNormal?.adsName = adsName
        }
        bannerDataLow?.let {
            bannerLow = BannerLoader(it.adsId, it.reloadTime, it.condition)
            bannerLow?.adsName = adsName
        }
    }

    private var container: FrameLayout? = null

    fun clearAds() {
        bannerHigh?.status = AdsUtils.Status.NONE

        bannerNormal?.status = AdsUtils.Status.NONE

        bannerLow?.status = AdsUtils.Status.NONE
    }

    private fun hasAvailableAds() = bannerHigh?.isSuccess() == true || bannerNormal?.isSuccess() == true || bannerLow?.isSuccess() == true

    fun showAds(activity: Activity, container: FrameLayout) {
        container.visibility = View.VISIBLE
        if (hasAvailableAds()) {
            if (bannerHigh?.isSuccess() == true) {
                Log.d(TAG, "showAds: high id(${bannerHigh?.idAds})")
                bannerHigh?.showAds(container)
            } else if (bannerNormal?.isSuccess() == true) {
                Log.d(TAG, "showAds: normal id(${bannerNormal?.idAds})")
                bannerNormal?.showAds(container)
            } else if (bannerLow?.isSuccess() == true) {
                Log.d(TAG, "showAds: low id(${bannerLow?.idAds})")
                bannerLow?.showAds(container)
            } else {
                container.visibility = View.GONE
            }
        } else {
            this@BannerAds.container = container
            this@BannerAds.container!!.findViewById<FrameLayout>(R.id.fl_shimemr).visibility = View.VISIBLE
            loadAds(activity, object : BannerAdListener() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    Log.d(TAG, "onAdFailedToLoad: Fail id(${p0.message})")
                    container.visibility = View.GONE
                }
            })
        }
    }

    fun loadAds(activity: Activity, listener: BannerAdListener? = null) {
        bannerHigh?.loadAds(activity, object : BannerAdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                Log.d(TAG, "bannerHigh:load success ${bannerHigh?.idAds}")
                listener?.onBannerLoaded(bannerHigh!!)
                if (container != null) {
                    bannerHigh?.showAds(container!!)
                    container = null
                }
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                if ((bannerNormal == null || bannerNormal!!.isFail()) && (bannerLow == null || bannerLow!!.isFail())) {
                    Log.d(TAG, "bannerHigh:load fail ${bannerHigh?.idAds}")
                    listener?.onAdFailedToLoad(p0)
                }
            }
        })

        bannerNormal?.loadAds(activity, object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                if (bannerHigh == null || bannerHigh!!.isFail()) {
                    Log.d(TAG, "bannerNormal:load success ${bannerNormal?.idAds}")
                    listener?.onBannerLoaded(bannerNormal!!)
                    if (container != null) {
                        bannerNormal?.showAds(container!!)
                        container = null
                    }
                }
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                if ((bannerHigh == null || bannerHigh!!.isFail()) && (bannerLow == null || bannerLow!!.isFail())) {
                    Log.d(TAG, "bannerNormal:load fail ${bannerNormal?.idAds}")
                    listener?.onAdFailedToLoad(p0)
                }
            }
        })

        bannerLow?.loadAds(activity, object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                if ((bannerHigh == null || bannerHigh!!.isFail()) && (bannerNormal == null || bannerNormal!!.isFail())) {
                    Log.d(TAG, "bannerLow:load success ${bannerLow?.idAds}")
                    listener?.onBannerLoaded(bannerLow!!)
                    if (container != null) {
                        bannerLow?.showAds(container!!)
                        container = null
                    }
                }
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                if ((bannerHigh == null || bannerHigh!!.isFail()) && (bannerNormal == null || bannerNormal!!.isFail())) {
                    Log.d(TAG, "bannerLow:load fail ${bannerLow?.idAds}")
                    listener?.onAdLoaded()
                }
            }
        })
    }

    class BannerLoader(
        val idAds: String, val timeReload: Int = 1, val condition: Boolean = true
    ) {
        var adsName: String = ""

        var adView: AdView? = null
        var status: AdsUtils.Status = AdsUtils.Status.NONE

        fun isLoading() = adView?.isLoading == true

        fun isSuccess() = status == AdsUtils.Status.SUCCESS

        fun isFail() = status == AdsUtils.Status.FAIL

        private fun isFailOrShownOrNoneAds() = status == AdsUtils.Status.NONE || status == AdsUtils.Status.SHOWN || status == AdsUtils.Status.FAIL

        fun showAds(container: FrameLayout) {
            if (condition && isSuccess()) {
                try {
                    container.findViewById<FrameLayout>(R.id.banner_container).removeAllViews()
                    container.findViewById<FrameLayout>(R.id.fl_shimemr).visibility = View.GONE
                    container.addView(adView)
                } catch (_: Exception) {

                }
            }
        }

        fun loadAds(activity: Activity, listener: AdListener? = null) {
            if (condition && isFailOrShownOrNoneAds() && AppUtils.isNetworkConnected(activity)) {
                loadAd(activity, timeReload, listener)
            }
        }

        fun loadAd(activity: Activity, countCanReload: Int, adListener: AdListener? = null) {
            if (countCanReload < 1) return
            adView = AdView(activity)
            val adRequest = AdRequest.Builder().build()
            adView!!.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    status = AdsUtils.Status.SUCCESS
                    adListener?.onAdLoaded()
                    Log.d(TAG, "onAdLoaded: $idAds")
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    Log.d(
                        TAG, "loadFail: fail times(${timeReload - countCanReload + 1}) id(${idAds})"
                    )
                    if (countCanReload - 1 > 0) {
                        loadAd(activity, countCanReload - 1, adListener)
                    } else {
                        status = AdsUtils.Status.FAIL
                        adListener?.onAdFailedToLoad(loadAdError)
                    }
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    status = AdsUtils.Status.SHOWN
                    FirebaseUtils.eventImpressionAds(adsName, idAds)
                    Log.d(TAG, "onAdImpression: $idAds")
                }
            }

            status = AdsUtils.Status.LOADING

            adView!!.adUnitId = idAds
            adView!!.setAdSize(adSize(activity))
            adView!!.loadAd(adRequest)
        }

        private fun adSize(activity: Activity): AdSize {
            val adWidthPixels = getScreenWidth(activity).toFloat()

            val density = activity.resources.displayMetrics.density
            val adWidth = (adWidthPixels / density).toInt()

            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                activity, adWidth
            )
        }

        private fun getScreenWidth(activity: Activity): Int {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val windowMetrics = activity.windowManager.currentWindowMetrics
                val insets: Insets = windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
                windowMetrics.bounds.width() - insets.left - insets.right
            } else {
                val displayMetrics = DisplayMetrics()
                activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
                displayMetrics.widthPixels
            }
        }
    }

    open class BannerAdListener : AdListener() {
        open fun onBannerLoaded(bannerLoader: BannerLoader) {

        }
    }

    companion object {
        private val TAG = BannerAds::class.java.simpleName
        fun FrameLayout.loadBanner(activity: Activity, idAdsBanner: String, condition: Boolean = true, adCallback: AdCallback? = null) {
            if (condition && AppUtils.isNetworkConnected(context)) {
                this.visibility = View.VISIBLE
                try {
                    this.findViewById<FrameLayout?>(R.id.banner_container)?.removeAllViews()
                    AperoAd.getInstance().loadBannerFragment(activity, idAdsBanner, this, object : AdCallback() {
                        override fun onAdFailedToLoad(i: LoadAdError?) {
                            super.onAdFailedToLoad(i)
                            this@loadBanner.visibility = View.GONE
                        }

                        override fun onAdFailedToShow(adError: AdError?) {
                            super.onAdFailedToShow(adError)
                            this@loadBanner.visibility = View.GONE
                        }

                        override fun onAdClicked() {
                            super.onAdClicked()
                            adCallback?.onAdClicked()
                        }

                        override fun onAdImpression() {
                            super.onAdImpression()
                            adCallback?.onAdImpression()
                        }
                    })
                } catch (ex: java.lang.Exception) {
                    this@loadBanner.visibility = View.GONE
                }
            } else {
                this.visibility = View.GONE
            }
        }

        fun FrameLayout.loadBannerCollapse(activity: Activity, idAdsBanner: String, condition: Boolean = true, adCallback: AdCallback? = null) {
            if (condition && AppUtils.isNetworkConnected(context)) {
                this.visibility = View.VISIBLE
                try {
                    this.findViewById<FrameLayout?>(R.id.banner_container)?.removeAllViews()
                    AperoAd.getInstance().loadCollapsibleBannerFragment(activity, idAdsBanner, this, "bottom", object : AdCallback() {
                        override fun onAdFailedToLoad(i: LoadAdError?) {
                            super.onAdFailedToLoad(i)
                            this@loadBannerCollapse.visibility = View.GONE
                        }

                        override fun onAdFailedToShow(adError: AdError?) {
                            super.onAdFailedToShow(adError)
                            this@loadBannerCollapse.visibility = View.GONE
                        }

                        override fun onAdClicked() {
                            super.onAdClicked()
                            adCallback?.onAdClicked()
                        }

                        override fun onAdImpression() {
                            super.onAdImpression()
                            adCallback?.onAdImpression()
                        }
                    })
                } catch (ex: java.lang.Exception) {
                    this@loadBannerCollapse.visibility = View.GONE
                }
            } else {
                this.visibility = View.GONE
            }
        }
    }
}