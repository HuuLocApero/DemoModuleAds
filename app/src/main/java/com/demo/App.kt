package com.demo

import com.ads.control.admob.Admob
import com.ads.control.ads.AperoAd
import com.ads.control.application.AdsMultiDexApplication
import com.ads.control.config.AdjustConfig
import com.ads.control.config.AperoAdConfig
import com.demo.di.AppInjector
import com.demo.utils.ads.AdsUtils
import com.demo.utils.PrefUtils
import com.demo.utils.firebase.FirebaseUtils
import com.demomoduleads.BuildConfig
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class App : AdsMultiDexApplication(), HasAndroidInjector {

    companion object {
        lateinit var app: App
        lateinit var adsUtils: AdsUtils
    }

    @Inject
    lateinit var prefUtils: PrefUtils

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
        val environment = if (BuildConfig.ENV_TEST) {
            AperoAdConfig.ENVIRONMENT_DEVELOP
        } else {
            AperoAdConfig.ENVIRONMENT_PRODUCTION
        }
        aperoAdConfig = AperoAdConfig(this, AperoAdConfig.PROVIDER_ADMOB, environment)
        aperoAdConfig.mediationProvider = AperoAdConfig.PROVIDER_ADMOB

        app = this
        aperoAdConfig.listDeviceTest = listOf(
            "577C9208AEFF7C67F9A420B37E32681F",
        )
        val adjustConfig = AdjustConfig("")
        aperoAdConfig.adjustConfig = adjustConfig

        aperoAdConfig.idAdResumeHigh = BuildConfig.resume_high
        aperoAdConfig.idAdResumeMedium = BuildConfig.resume_medium
        aperoAdConfig.idAdResume = BuildConfig.resume_low


        AperoAd.getInstance().init(this, aperoAdConfig, false)

        Admob.getInstance().setFan(false)
        Admob.getInstance().setAppLovin(false)
        Admob.getInstance().setColony(false)
        Admob.getInstance().setOpenActivityAfterShowInterAds(true)
        Admob.getInstance().setDisableAdResumeWhenClickAds(true)

        //Firebase analytics
        FirebaseUtils.init(this)
        adsUtils = AdsUtils()
        prefUtils.resetData()
    }

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}