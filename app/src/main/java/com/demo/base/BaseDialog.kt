package com.demo.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.demomoduleads.BR
import com.demomoduleads.R
import com.demo.ui.activities.MainViewModel
import com.demo.utils.LanguageUtils
import com.demo.utils.PrefUtils
import com.demo.utils.firebase.EventClick
import dagger.android.AndroidInjection
import dagger.android.support.DaggerDialogFragment
import javax.inject.Inject

open class BaseDialog<VM : BaseViewModel, DB : ViewDataBinding> @Inject constructor(
    @LayoutRes val layout: Int, viewModelClass: Class<VM>
) : DaggerDialogFragment(), IBaseUI {

    @Inject
    lateinit var prefUtils: PrefUtils

    open lateinit var binding: DB

    private var iBase: IBaseUI? = null

    open fun initView() {}

    open fun initData() {}

    open fun initAds() {}

    open fun initListener() {}

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val mainViewModel: MainViewModel by activityViewModels { viewModelFactory }

    val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[viewModelClass]
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*, *>) {
            iBase = context
        }
    }

    open fun init(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, layout, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.setVariable(BR.viewModel, viewModel)
        binding.setVariable(BR.prefUtils, prefUtils)
        binding.setVariable(BR.fragment, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(activity)
        super.onCreate(savedInstanceState)
        isCancelable = false
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        LanguageUtils.getDefaultLanguage(requireContext())
        init(inflater, container)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAds()
        initView()
        initData()
        initListener()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    override fun onResume() {
        super.onResume()
        iBase?.hideSystemNavigationBar()
    }

    override fun showLoading(content: String) {
        iBase?.showLoading(content)
    }

    override fun hideLoading() {
        iBase?.hideLoading()
    }

    override fun isNetworkConnected(): Boolean {
        return iBase?.isNetworkConnected() ?: false
    }

    override fun hideSystemNavigationBar() {
        iBase?.hideSystemNavigationBar()
    }

    override fun showToast(content: String) {
        iBase?.showToast(content)
    }

    override fun hideKeyboard(view: View?) {
        iBase?.hideKeyboard(view)
    }

    override fun checkPermission(permission: String): Boolean {
        return iBase?.checkPermission(permission) ?: false
    }

    override fun disableAdsResume() {
        iBase?.disableAdsResume()
    }

    override fun disableAdsResumeByClickAction() {
        iBase?.disableAdsResumeByClickAction()
    }

    override fun enableAdsResume() {
        iBase?.enableAdsResume()
    }

    override fun trackEvent(event: EventClick, param: String?, value: String?) {
        iBase?.trackEvent(event, param, value)
    }
}