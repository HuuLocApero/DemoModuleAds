package com.demo.ui.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.demomoduleads.R
import com.demo.ui.fragments.onboarding.OnBoardingPageFragment
import com.demo.ui.fragments.onboarding.Onboarding

class OnBoardingPageAdapter(val context: Context, fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        val PAGE_INDEX_1 = 0
        val PAGE_INDEX_2 = 1
        val PAGE_INDEX_3 = 2

        val NUMBER_PAGE = 3
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            PAGE_INDEX_1 -> OnBoardingPageFragment.newInstance(Onboarding(R.string.title_onboarding_1, R.string.content_onboarding_1))
            PAGE_INDEX_2 -> OnBoardingPageFragment.newInstance(Onboarding(R.string.title_onboarding_2, R.string.content_onboarding_2 ))
            else -> OnBoardingPageFragment.newInstance(Onboarding(R.string.title_onboarding_3, R.string.content_onboarding_3))
        }
    }

    override fun getCount(): Int {
        return NUMBER_PAGE
    }
}