package com.demo.ui.activities

import androidx.activity.viewModels
import com.demomoduleads.R
import com.demo.base.BaseActivity
import com.demomoduleads.databinding.ActivityMainBinding
import com.demo.ui.fragments.home.HomeViewModel

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(
    R.layout.activity_main, MainViewModel::class.java
) {

    private val sharedViewModel: HomeViewModel by viewModels { viewModelProviderFactory }

}