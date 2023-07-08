package com.demo.ui.fragments.onboarding

import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.demo.base.BaseFragment
import com.demo.base.BaseViewModel
import com.demo.ui.adapters.OnBoardingPageAdapter
import com.demo.utils.ViewUtils.clickWithDebounce
import com.demo.utils.firebase.EventClick
import com.demomoduleads.R
import com.demomoduleads.databinding.FragmentOnboardingBinding

class OnBoardingFragment : BaseFragment<BaseViewModel, FragmentOnboardingBinding>(R.layout.fragment_onboarding, BaseViewModel::class.java) {
    override fun backPressedWithExitPopup() = true

    override fun initAds() {
        adsUtils.nativeOnBoarding.showAds(requireActivity(), binding.flAds, false)
    }

    override fun clearAds() {
        adsUtils.nativeOnBoarding.clearAds()
    }

    override fun initView() {
        trackEvent(EventClick.SCR_ONBOARD)
        binding.run {
            val adapter = OnBoardingPageAdapter(requireContext(), childFragmentManager)
            binding.viewPager.adapter = adapter
            binding.tabLayout.setupWithViewPager(binding.viewPager)
        }
    }

    override fun initListener() {
        binding.run {
            binding.viewPager.addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {
                    binding.tvStart.setText(if (position != 2) R.string.next else R.string.start)
                }

                override fun onPageScrollStateChanged(state: Int) {

                }
            })

            tvStart.clickWithDebounce {
                if (binding.viewPager.currentItem < 2) {
                    trackEvent(EventClick.CLICK_NEXT_SCR_ONBOARD)
                    binding.viewPager.currentItem++
                } else {
                    trackEvent(EventClick.CLICK_START_SCR_ONBOARD)
                    prefUtils.isShowOnBoardingFirstOpen = false

                    val action = OnBoardingFragmentDirections.actionOnBoardingFragmentToHomeFragment()
                    safeNav(action)
                }
            }
        }
    }
}