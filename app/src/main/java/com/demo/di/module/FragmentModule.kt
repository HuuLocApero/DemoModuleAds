package com.demo.di.module

import com.demo.ui.dialog.LoadingDialog
import com.demo.ui.dialog.NoInternetPopup
import com.demo.ui.dialog.PopupExit
import com.demo.ui.dialog.YesNoPopup
import com.demo.ui.fragments.demo_ads.InterstitialFragment
import com.demo.ui.fragments.demo_ads.NativeFragment
import com.demo.ui.fragments.demo_ads.RewardFragment
import com.demo.ui.fragments.demo_ads.nextfragment.NextFragment
import com.demo.ui.fragments.home.HomeFragment
import com.demo.ui.fragments.language.LanguageFragment
import com.demo.ui.fragments.onboarding.OnBoardingFragment
import com.demo.ui.fragments.onboarding.OnBoardingPageFragment
import com.demo.ui.fragments.splash.SplashFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun splashFragment(): SplashFragment

    @ContributesAndroidInjector
    abstract fun languageFragment(): LanguageFragment

    @ContributesAndroidInjector
    abstract fun onBoardingFragment(): OnBoardingFragment

    @ContributesAndroidInjector
    abstract fun onBoardingPageFragment(): OnBoardingPageFragment

    @ContributesAndroidInjector
    abstract fun homeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun popupExit(): PopupExit

    @ContributesAndroidInjector
    abstract fun noInternetPopup(): NoInternetPopup

    @ContributesAndroidInjector
    abstract fun loadingDialog(): LoadingDialog

    @ContributesAndroidInjector
    abstract fun yesNoPopup(): YesNoPopup

    @ContributesAndroidInjector
    abstract fun nativeFragment(): NativeFragment

    @ContributesAndroidInjector
    abstract fun interstitialFragment(): InterstitialFragment

    @ContributesAndroidInjector
    abstract fun rewardFragment(): RewardFragment

    @ContributesAndroidInjector
    abstract fun nextFragment(): NextFragment
}