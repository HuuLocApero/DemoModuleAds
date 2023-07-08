package com.demo.ui.fragments.demo_ads

import com.demomoduleads.R
import com.demomoduleads.databinding.FragmentNativeBinding
import com.demo.base.BaseFragment
import com.demo.base.BaseViewModel
import com.demo.utils.customview.HeaderView

class NativeFragment : BaseFragment<BaseViewModel, FragmentNativeBinding>(
    R.layout.fragment_native, BaseViewModel::class.java
), HeaderView.Listener {

    override fun initAds() {
        binding.run {
            adsUtils.nativeWithControllerControll.showAds(requireActivity(), binding.flAdsControl)
            adsUtils.nativeWithControllerMedium.showAds(requireActivity(), binding.flAdsMedium)
            adsUtils.nativeWithControllerSmall.showAds(requireActivity(), binding.flAdsSmall)

            adsUtils.nativeLoadInScreenControll.showAds(requireActivity(), binding.flAdsControlLoadInScreen)
            adsUtils.nativeLoadInScreenMedium.showAds(requireActivity(), binding.flAdsMediumLoadInScreen)
            adsUtils.nativeLoadInScreenSmall.showAds(requireActivity(), binding.flAdsSmallLoadInScreen)
        }
    }

    override fun clearAds() {
        adsUtils.nativeLoadInScreenControll.clearAds()
        adsUtils.nativeLoadInScreenMedium.clearAds()
        adsUtils.nativeLoadInScreenSmall.clearAds()
    }

    override fun onHeaderBackPressed() {
        safeBackNav()
    }
}