package com.demo.ui.fragments.demo_ads

import com.demomoduleads.R
import com.demomoduleads.databinding.FragmentInterstitialBinding
import com.demo.base.BaseFragment
import com.demo.base.BaseViewModel
import com.demo.utils.customview.HeaderView

class InterstitialFragment : BaseFragment<BaseViewModel, FragmentInterstitialBinding>(
    R.layout.fragment_interstitial, BaseViewModel::class.java
), HeaderView.Listener {

    override fun initAds() {
        adsUtils.interWithController.loadAds(requireContext())
        adsUtils.interWithoutController.loadAds(requireContext())
        adsUtils.interWithoutControllerNotReload.loadAds(requireContext())
    }

    override fun clearAds() {
        adsUtils.interWithoutControllerNotReload.clearAds()
    }

    override fun initListener() {
        binding.run {
            withController.setOnClickListener {
                adsUtils.interWithController.show(requireContext(), true) {
                    safeNav(InterstitialFragmentDirections.actionInterstitialFragmentToNextFragment())
                }
            }

            withoutController.setOnClickListener {
                adsUtils.interWithoutController.show(requireContext(), true) {
                    safeNav(InterstitialFragmentDirections.actionInterstitialFragmentToNextFragment())
                }
            }

            withoutControllerNotReloadAfterShow.setOnClickListener {
                adsUtils.interWithoutControllerNotReload.show(requireContext(), false) {
                    safeNav(InterstitialFragmentDirections.actionInterstitialFragmentToNextFragment())
                }
            }
        }
    }

    override fun onHeaderBackPressed() {
        safeBackNav()
    }
}