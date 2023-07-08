package com.demo.utils.ads

import com.demomoduleads.BuildConfig
import com.demomoduleads.R
import com.demo.utils.PrefUtils
import com.demo.utils.firebase.EventClick

class AdsUtils {
    companion object {
        val TAG = AdsUtils::class.java.simpleName

        val prefUtils = PrefUtils.instant

        // if u use ads controller, u add rewardLoaderHigh/rewardLoaderNormal to rewardAll
        // and if not, remove rewardLoaderHigh/rewardLoaderNormal, but keep rewardAll
        val rewardAll: RewardAds = RewardAds(
            adsName = "reward_all",
            rewardLoaderHigh = RewardAds.RewardLoader(BuildConfig.reward_test_high, 3, condition = true, isRewardInterstitial = false),
            rewardLoaderNormal = RewardAds.RewardLoader(BuildConfig.reward_test_medium, 3, condition = true, isRewardInterstitial = false),
        )

        // if u use ads controller, u add interLoaderHigh/interLoaderNormal to rewardAll
        // and if not, remove interLoaderHigh/interLoaderNormal, but keep rewardAll
        val interAll: InterstitialAds = InterstitialAds(
            adsName = "inter_all",
            interLoaderHigh = InterstitialAds.InterstitialLoader(BuildConfig.inter_high, 3, true),
            interLoaderNormal = InterstitialAds.InterstitialLoader(BuildConfig.inter_medium, 3, true),
        )

        // if u use ads controller, u add nativeLoaderHigh/nativeLoaderNormal to rewardAll
        // and if not, remove nativeLoaderHigh/nativeLoaderNormal, but keep rewardAll
        val nativeAll = NativeAds(
            R.layout.native_control,
            adsName = "native_all",
            nativeLoaderHigh = NativeAds.NativeLoader(BuildConfig.native_high, 3, true),
            nativeLoaderNormal = NativeAds.NativeLoader(BuildConfig.native_medium, 3, true),
        )
    }

    // if it use with controller above, u can set useController on/off to use this
    // it has 3 params ads: interLoaderHigh/interLoaderNormal/interLoaderLow
    // u can set condition show/load to InterstitialLoader
    // adsName/eventView/eventClick use for log tracking event
    val interWithController: InterstitialAds = InterstitialAds(
        interLoaderHigh = InterstitialAds.InterstitialLoader(BuildConfig.inter_test_high, 1, true),
        adsName = "inter_test",
        eventView = EventClick.DISPLAY_AD_INTER_ONBOARD.content,
        eventClick = EventClick.CLICK_AD_INTER_ONBOARD.content,
        useController = true
    )

    val interWithoutController: InterstitialAds = InterstitialAds(
        interLoaderHigh = InterstitialAds.InterstitialLoader(BuildConfig.inter_test_high, 1, true),
        adsName = "inter_test",
        eventView = EventClick.DISPLAY_AD_INTER_ONBOARD.content,
        eventClick = EventClick.CLICK_AD_INTER_ONBOARD.content,
        useController = false
    )

    val interWithoutControllerNotReload: InterstitialAds = InterstitialAds(
        interLoaderHigh = InterstitialAds.InterstitialLoader(BuildConfig.inter_test_high, 1, true),
        adsName = "inter_test",
        eventView = EventClick.DISPLAY_AD_INTER_ONBOARD.content,
        eventClick = EventClick.CLICK_AD_INTER_ONBOARD.content,
        useController = false
    )

    // same behavior with interstitial ads
    val rewardTypeReward: RewardAds = RewardAds(
        rewardLoaderHigh = RewardAds.RewardLoader(BuildConfig.reward_test_high, 1, condition = true, isRewardInterstitial = false),
        adsName = "inter_test",
        eventView = EventClick.DISPLAY_AD_INTER_ONBOARD.content,
        eventClick = EventClick.CLICK_AD_INTER_ONBOARD.content,
        useController = false
    )

    val rewardTypeInterstitial: RewardAds = RewardAds(
        rewardLoaderHigh = RewardAds.RewardLoader(BuildConfig.reward_type_interstitial, 1, condition = true, isRewardInterstitial = true),
        adsName = "inter_test",
        eventView = EventClick.DISPLAY_AD_INTER_ONBOARD.content,
        eventClick = EventClick.CLICK_AD_INTER_ONBOARD.content,
        useController = true
    )

    // same behavior with interstitial ads
    val nativeLanguage: NativeAds = NativeAds(
        R.layout.native_control,
        useController = false,
        nativeLoaderHigh = NativeAds.NativeLoader(BuildConfig.native_language_high, 1, true),
        nativeLoaderNormal = NativeAds.NativeLoader(BuildConfig.native_language_medium, 1, true),
        nativeLoaderLow = NativeAds.NativeLoader(BuildConfig.native_language_low, 1, true),
        adsName = "native_language",
        eventView = EventClick.DISPLAY_AD_SCR_LANGUAGE.content,
        eventClick = EventClick.CLICK_AD_SCR_LANGUAGE.content
    )
    val nativeOnBoarding: NativeAds = NativeAds(
        R.layout.native_medium,
        useController = false,
        nativeLoaderHigh = NativeAds.NativeLoader(BuildConfig.native_onboarding_high, 1, true),
        nativeLoaderNormal = NativeAds.NativeLoader(BuildConfig.native_onboarding_medium, 1, true),
        nativeLoaderLow = NativeAds.NativeLoader(BuildConfig.native_onboarding_low, 1, true),
        adsName = "native_onboarding",
        eventView = EventClick.DISPLAY_AD_SCR_ONBOARD.content,
        eventClick = EventClick.CLICK_AD_SCR_ONBOARD.content
    )

    val nativeWithControllerControll: NativeAds = NativeAds(
        R.layout.native_control,
        useController = true,
        nativeLoaderHigh = NativeAds.NativeLoader(BuildConfig.native_onboarding_high, 1, true),
        adsName = "nativeWithController",
        eventView = EventClick.DISPLAY_AD_SCR_ONBOARD.content,
        eventClick = EventClick.CLICK_AD_SCR_ONBOARD.content
    )

    val nativeWithControllerMedium: NativeAds = NativeAds(
        R.layout.native_medium,
        useController = true,
        nativeLoaderHigh = NativeAds.NativeLoader(BuildConfig.native_onboarding_high, 1, true),
        adsName = "nativeWithController",
        eventView = EventClick.DISPLAY_AD_SCR_ONBOARD.content,
        eventClick = EventClick.CLICK_AD_SCR_ONBOARD.content
    )

    val nativeWithControllerSmall: NativeAds = NativeAds(
        R.layout.native_small,
        useController = true,
        nativeLoaderHigh = NativeAds.NativeLoader(BuildConfig.native_onboarding_high, 1, true),
        adsName = "nativeWithController",
        eventView = EventClick.DISPLAY_AD_SCR_ONBOARD.content,
        eventClick = EventClick.CLICK_AD_SCR_ONBOARD.content
    )

    val nativeLoadInScreenControll: NativeAds = NativeAds(
        R.layout.native_control,
        useController = false,
        nativeLoaderHigh = NativeAds.NativeLoader(BuildConfig.native_onboarding_high, 1, true),
        adsName = "nativeWithController",
        eventView = EventClick.DISPLAY_AD_SCR_ONBOARD.content,
        eventClick = EventClick.CLICK_AD_SCR_ONBOARD.content
    )

    val nativeLoadInScreenMedium: NativeAds = NativeAds(
        R.layout.native_medium,
        useController = false,
        nativeLoaderHigh = NativeAds.NativeLoader(BuildConfig.native_onboarding_high, 1, true),
        adsName = "nativeWithController",
        eventView = EventClick.DISPLAY_AD_SCR_ONBOARD.content,
        eventClick = EventClick.CLICK_AD_SCR_ONBOARD.content
    )

    val nativeLoadInScreenSmall: NativeAds = NativeAds(
        R.layout.native_small,
        useController = false,
        nativeLoaderHigh = NativeAds.NativeLoader(BuildConfig.native_onboarding_high, 1, true),
        adsName = "nativeWithController",
        eventView = EventClick.DISPLAY_AD_SCR_ONBOARD.content,
        eventClick = EventClick.CLICK_AD_SCR_ONBOARD.content
    )

    enum class Status {
        LOADING, NONE, FAIL, SUCCESS, SHOWN
    }


    fun updateAdvertisementShowingCondition() {
        interWithController.interLoaderHigh?.condition = true
        interWithoutController.interLoaderHigh?.condition = true

        nativeLanguage.nativeLoaderHigh?.condition = true
        nativeOnBoarding.nativeLoaderHigh?.condition = true
    }
}