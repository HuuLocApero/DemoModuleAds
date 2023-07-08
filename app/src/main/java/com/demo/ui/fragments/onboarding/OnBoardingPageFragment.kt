package com.demo.ui.fragments.onboarding

import android.os.Bundle
import com.demomoduleads.R
import com.demomoduleads.databinding.FragmentOnboardingPageBinding
import com.demo.base.BaseFragment
import com.demo.base.BaseViewModel
import com.demo.utils.ViewUtils.setGradiantText

class OnBoardingPageFragment : BaseFragment<BaseViewModel, FragmentOnboardingPageBinding>(
    R.layout.fragment_onboarding_page,
    BaseViewModel::class.java
) {
    companion object {
        const val TITLE = "title"
        const val CONTENT = "content"
        fun newInstance(onBoarding: Onboarding): OnBoardingPageFragment {

            val bundle = Bundle()
            bundle.putInt(TITLE, onBoarding.title)
            bundle.putInt(CONTENT, onBoarding.content)

            val fragment = OnBoardingPageFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView() {
        val title = requireArguments().getInt(TITLE)
        val content = requireArguments().getInt(CONTENT)

        binding.tvTitle.setText(title)
        binding.tvTitle.setGradiantText("#A7FFEF", "#48079C")

        binding.tvContent.setText(content)
    }
}