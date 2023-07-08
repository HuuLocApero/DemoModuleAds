package com.demo.ui.fragments.splash

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.ads.control.ads.AperoAd
import com.ads.control.ads.AperoAdCallback
import com.ads.control.ads.wrapper.ApAdError
import com.demomoduleads.BuildConfig
import com.demomoduleads.R
import com.demomoduleads.databinding.FragmentSplashScreenBinding
import com.demo.base.BaseActivity
import com.demo.base.BaseFragment
import com.demo.common.Constant
import com.demo.utils.PrefUtils
import com.demo.utils.ads.AdsUtils
import com.demo.utils.firebase.EventClick
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class SplashFragment : BaseFragment<SplashViewModel, FragmentSplashScreenBinding>(
    R.layout.fragment_splash_screen, SplashViewModel::class.java
) {

    companion object {
        private enum class SplashType { INTERSTITIAL_HIGH, INTERSTITIAL_NORMAL, FAIL }
        private enum class FetchRemoteConfig { FETCHING, DONE, NONE }
    }

    private var splashLoadType: SplashType? = null
    private var fetchRemoteConfig: FetchRemoteConfig = FetchRemoteConfig.NONE
    private var errorShowAdsInBackground: Boolean = false
    private var finishedSplash = false

    override fun initAds() {
        // load ads controller
        AdsUtils.nativeAll.loadAds(requireActivity())
        AdsUtils.interAll.loadAds(requireActivity())
        AdsUtils.rewardAll.loadAds(requireActivity())

        //pre-load in splash
        adsUtils.nativeWithControllerControll.loadAds(requireActivity())

        disableAdsResume()
    }

    override fun initData() {
        trackEvent(EventClick.SCR_SPLASH)
        if (openByChangeLanguage()) {
            safeNav(SplashFragmentDirections.actionSplashFragmentToHomeFragment())
        }
    }

    private fun setupRemoteConfig() {
        fetchRemoteConfig = FetchRemoteConfig.FETCHING
        val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder().setFetchTimeoutInSeconds(5000)
            .setMinimumFetchIntervalInSeconds(if (BuildConfig.ENV_TEST) 3600 else 0).build()
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)

        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                fetchDataRemote(firebaseRemoteConfig)
            }
            fetchRemoteConfig = FetchRemoteConfig.DONE
            onShowSplashScreen()
        }.addOnFailureListener {
            fetchRemoteConfig = FetchRemoteConfig.DONE
            onShowSplashScreen()
        }
        loadSplash()
    }

    private fun fetchDataRemote(firebaseRemoteConfig: FirebaseRemoteConfig) {
        prefUtils.banner = firebaseRemoteConfig.getBoolean(PrefUtils.BANNER)
        prefUtils.reward = firebaseRemoteConfig.getBoolean(PrefUtils.REWARD)
        prefUtils.inter = firebaseRemoteConfig.getBoolean(PrefUtils.INTER)
        prefUtils.native = firebaseRemoteConfig.getBoolean(PrefUtils.NATIVE)

        adsUtils.updateAdvertisementShowingCondition()
    }

    private fun onShowSplashScreen() {
        if (fetchRemoteConfig == FetchRemoteConfig.DONE) {
            if (splashLoadType == SplashType.INTERSTITIAL_HIGH) {
                AperoAd.getInstance().onShowSplashPriority(requireActivity() as AppCompatActivity, object : AperoAdCallback() {
                    override fun onNextAction() {
                        super.onNextAction()
                        goToMainScreen()
                    }

                    override fun onAdFailedToShow(adError: ApAdError?) {
                        super.onAdFailedToShow(adError)
                        if (requireActivity().lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                            goToMainScreen()
                        } else {
                            errorShowAdsInBackground = true
                        }
                        Log.d(TAG, "onAdFailedToShow:INTERSTITIAL_HIGH ${adError?.message}")
                    }

                    override fun onAdClosed() {
                        super.onAdClosed()
                        goToMainScreen()
                    }

                    override fun onAdImpression() {
                        super.onAdImpression()
                        trackEvent(EventClick.DISPLAY_AD_SCR_SPLASH)
                    }

                    override fun onAdClicked() {
                        super.onAdClicked()
                        trackEvent(EventClick.CLICK_AD_SCR_SPLASH)
                    }
                })
            } else if (splashLoadType == SplashType.INTERSTITIAL_NORMAL) {
                AperoAd.getInstance().onShowSplash(requireActivity() as AppCompatActivity, object : AperoAdCallback() {
                    override fun onNextAction() {
                        super.onNextAction()
                        goToMainScreen()
                    }

                    override fun onAdFailedToShow(adError: ApAdError?) {
                        super.onAdFailedToShow(adError)
                        if (requireActivity().lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                            goToMainScreen()
                        } else {
                            errorShowAdsInBackground = true
                        }
                        Log.d(TAG, "onAdFailedToShow:INTERSTITIAL_NORMAL ${adError?.message}")
                    }

                    override fun onAdClosed() {
                        super.onAdClosed()
                        goToMainScreen()
                    }
                })
            } else if (SplashType.FAIL == splashLoadType) {
                goToMainScreen()
            }
        }
    }

    private fun loadSplash() {
        if (isNetworkConnected() && context != null) {
            AperoAd.getInstance().loadSplashInterPrioritySameTime(requireContext(),
                BuildConfig.inter_splash_high,
                BuildConfig.inter_splash_medium,
                30000,
                5000,
                false,
                object : AperoAdCallback() {

                    override fun onAdSplashReady() {
                        super.onAdSplashReady()
                        if (!isAdded) {
                            return
                        }
                        splashLoadType = SplashType.INTERSTITIAL_HIGH
                        onShowSplashScreen()
                        Log.d(TAG, "onAdSplashReady: ")
                    }

                    override fun onNormalInterSplashLoaded() {
                        super.onNormalInterSplashLoaded()
                        if (!isAdded) {
                            return
                        }
                        splashLoadType = SplashType.INTERSTITIAL_NORMAL
                        onShowSplashScreen()
                        Log.d(TAG, "onNormalInterSplashLoaded: ")
                    }

                    override fun onAdPriorityFailedToLoad(adError: ApAdError?) {
                        super.onAdPriorityFailedToLoad(adError)
                        splashLoadType = SplashType.FAIL
                        goToMainScreen()
                        Log.d(TAG, "onAdFailedToLoad: ${adError?.message}")
                    }

                    override fun onAdPriorityMediumFailedToLoad(adError: ApAdError?) {
                        super.onAdPriorityMediumFailedToLoad(adError)
                        splashLoadType = SplashType.FAIL
                        goToMainScreen()
                        Log.d(TAG, "onAdFailedToLoad: ${adError?.message}")
                    }

                    override fun onAdFailedToLoad(adError: ApAdError?) {
                        super.onAdFailedToLoad(adError)
                        splashLoadType = SplashType.FAIL
                        goToMainScreen()
                        Log.d(TAG, "onAdFailedToLoad: ${adError?.message}")
                    }

                    override fun onNextAction() {
                        super.onNextAction()
                        if (fetchRemoteConfig == FetchRemoteConfig.DONE) {
                            goToMainScreen()
                        }
                    }
                })
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                goToMainScreen()
            }, 2000)
        }
    }

    private var handler = Handler(Looper.getMainLooper())
    private var runnableSplash = Runnable { this.onShowSplashScreen() }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
        Log.d(TAG, "onPause: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
        if (errorShowAdsInBackground) {
            errorShowAdsInBackground = false
            if (isNetworkConnected()) {
                if (splashLoadType == SplashType.INTERSTITIAL_HIGH || splashLoadType == SplashType.INTERSTITIAL_NORMAL) {
                    handler.postDelayed(runnableSplash, 1000)
                } else {
                    goToMainScreen()
                }
            } else {
                goToMainScreen()
            }
            return
        }

        // check conditional
        if (fetchRemoteConfig == FetchRemoteConfig.NONE && !openByChangeLanguage()) {
            setupRemoteConfig()
        }

        //if go onResume but not have action
        if (!finishedSplash && fetchRemoteConfig == FetchRemoteConfig.DONE) {
            AperoAd.getInstance().onCheckShowSplashPriority3WhenFail(
                requireActivity() as AppCompatActivity, object : AperoAdCallback() {
                    override fun onNextAction() {
                        super.onNextAction()
                        goToMainScreen()
                    }

                    override fun onAdClosed() {
                        super.onAdClosed()
                        goToMainScreen()
                    }

                    override fun onAdFailedToShow(adError: ApAdError?) {
                        super.onAdFailedToShow(adError)
                        goToMainScreen()
                    }

                }, 1000
            )
        }
    }

    private fun goToMainScreen() {
        if (!finishedSplash) {
            if ((requireActivity() as? BaseActivity<*, *>)?.isOnResume == false) {
                errorShowAdsInBackground = true
            }
            if (prefUtils.isShowLanguageFirstOpen) {
                val destination = SplashFragmentDirections.actionSplashFragmentToLanguageFragment()
                safeNav(destination)
            } else {
                if (prefUtils.isShowOnBoardingFirstOpen) {
                    val action = SplashFragmentDirections.actionSplashFragmentToOnBoardingFragment()
                    safeNav(action)
                } else {
                    val action = SplashFragmentDirections.actionSplashFragmentToHomeFragment()
                    safeNav(action)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        enableAdsResume()
        finishedSplash = true
    }

    private fun openByChangeLanguage(): Boolean {
        if (requireActivity().intent.hasExtra(Constant.KEY_START_FROM_LANGUAGE)) {
            return requireActivity().intent.getBooleanExtra(Constant.KEY_START_FROM_LANGUAGE, false)
        }
        return false
    }
}