package com.demo.ui.fragments.home

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import com.ads.control.funtion.AdCallback
import com.demomoduleads.BuildConfig
import com.demomoduleads.R
import com.demomoduleads.databinding.FragmentHomeBinding
import com.demo.base.BaseFragment
import com.demo.base.BaseViewModel
import com.demo.network.exception.NoConnectivityException
import com.demo.ui.dialog.YesNoPopup
import com.demo.utils.ads.BannerAds.Companion.loadBanner
import com.demo.utils.ads.BannerAds.Companion.loadBannerCollapse
import com.demo.utils.customview.HeaderView
import com.demo.utils.firebase.EventClick

class HomeFragment : BaseFragment<BaseViewModel, FragmentHomeBinding>(
    R.layout.fragment_home, BaseViewModel::class.java
), IHomeUi, HeaderView.Listener {

    override fun backPressedWithExitPopup() = true
    override fun checkNoInternet(): Boolean {
        return true
    }

    private val homeViewModel: HomeViewModel by activityViewModels { viewModelFactory }

    companion object {
        private var tabId: Int = R.id.generateFragment
    }

    override fun initAds() {
        adsUtils.rewardTypeReward.loadAds(requireActivity())
        adsUtils.rewardTypeInterstitial.loadAds(requireActivity())

        adsUtils.interWithController.loadAds(requireContext())
        adsUtils.interWithoutController.loadAds(requireContext())
        adsUtils.interWithoutControllerNotReload.loadAds(requireContext())
    }

    override fun initData() {
        homeViewModel.fetchData()
    }

    override fun initListener() {
        super.initListener()
        homeViewModel.run {
            error.observe(this@HomeFragment) {
                if (it.ex is NoConnectivityException) {
                    YesNoPopup.showPopup(childFragmentManager,
                        getString(R.string.ooops),
                        getString(R.string.no_internet_connection_found),
                        getString(R.string.try_again),
                        getString(R.string.cancel_text),
                        object : YesNoPopup.YesNoListener {
                            override fun onContinuePresses() {
                                homeViewModel.fetchData()
                            }
                        })
                } else {
                    showToast(it.messagge)
                }
            }
        }

        binding.run {
            nativeAds.setOnClickListener {
                safeNav(HomeFragmentDirections.actionHomeFragmentToNativeFragment())
            }

            interAds.setOnClickListener {
                safeNav(HomeFragmentDirections.actionHomeFragmentToInterstitialFragment())

            }

            rewardAds.setOnClickListener {
                safeNav(HomeFragmentDirections.actionHomeFragmentToRewardFragment())

            }

            banner.setOnClickListener {
                loadBanner()
            }

            bannerCollapse.setOnClickListener {
                loadBannerCollapse()
            }
        }
    }

    private fun loadBanner() {
        binding.flBanner.loadBanner(requireActivity(), BuildConfig.banner, prefUtils.banner, object : AdCallback() {
            override fun onAdImpression() {
                super.onAdImpression()
                trackEvent(EventClick.DISPLAY_AD_SCR_BANNER_GENERATE, "ad_format", "banner")
            }

            override fun onAdClicked() {
                super.onAdClicked()
                trackEvent(EventClick.CLICK_AD_SCR_BANNER_GENERATE, "ad_format", "banner")
            }
        })
    }

    private fun loadBannerCollapse() {
        binding.flBanner.loadBannerCollapse(requireActivity(), BuildConfig.banner, prefUtils.banner, object : AdCallback() {
            override fun onAdImpression() {
                super.onAdImpression()
                trackEvent(EventClick.DISPLAY_AD_SCR_BANNER_GENERATE, "ad_format", "banner")
            }

            override fun onAdClicked() {
                super.onAdClicked()
                trackEvent(EventClick.CLICK_AD_SCR_BANNER_GENERATE, "ad_format", "banner")
            }
        })
    }

    override fun onOptionPressed(view: View) {
        trackEvent(EventClick.CLICK_SETTING)
    }

    override fun navigateTo(action: NavDirections) {
        safeNav(action)
    }
}