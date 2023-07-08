package com.demo.ui.fragments.demo_ads.nextfragment

import com.demomoduleads.R
import com.demomoduleads.databinding.FragmentNextBinding
import com.demo.base.BaseFragment
import com.demo.base.BaseViewModel
import com.demo.utils.customview.HeaderView

class NextFragment : BaseFragment<BaseViewModel, FragmentNextBinding>(
    R.layout.fragment_next, BaseViewModel::class.java
), HeaderView.Listener {

    override fun onHeaderBackPressed() {
        safePopBackStackNav(R.id.homeFragment, inclusive = false, saveState = true)
    }
}