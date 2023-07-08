package com.demo.ui.fragments.demo_ads

import com.demomoduleads.R
import com.demomoduleads.databinding.FragmentRewardBinding
import com.demo.base.BaseFragment
import com.demo.base.BaseViewModel
import com.demo.utils.customview.HeaderView

class RewardFragment : BaseFragment<BaseViewModel, FragmentRewardBinding>(
    R.layout.fragment_reward, BaseViewModel::class.java
), HeaderView.Listener {

    override fun initAds() {
        adsUtils.rewardTypeReward.loadAds(requireActivity())
        adsUtils.rewardTypeInterstitial.loadAds(requireActivity())
    }

    override fun initListener() {
        binding.run {
            rewardAds.setOnClickListener {
                adsUtils.rewardTypeReward.show(requireActivity(), true) {
                    safeNav(RewardFragmentDirections.actionRewardFragmentToNextFragment())
                }
            }

            rewardAdsInters.setOnClickListener {
                adsUtils.rewardTypeInterstitial.show(requireActivity(), true) {
                    safeNav(RewardFragmentDirections.actionRewardFragmentToNextFragment())
                }
            }
        }
    }

    override fun onHeaderBackPressed() {
        safeBackNav()
    }
}