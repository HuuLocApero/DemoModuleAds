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
import com.demo.ui.activities.MainViewModel
import com.demo.utils.LanguageUtils
import com.demo.utils.PrefUtils
import com.demo.utils.firebase.EventClick
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


open class BaseBottomSheetDialog<VM : BaseViewModel, DB : ViewDataBinding> @Inject constructor(
    @LayoutRes val layout: Int, viewModelClass: Class<VM>
) : BottomSheetDialogFragment(), IBaseUI {

    val TAG = this::class.java.simpleName

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

    val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[viewModelClass]
    }

    val mainViewModel: MainViewModel by activityViewModels { viewModelFactory }

    open fun init(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, layout, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.setVariable(BR.viewModel, viewModel)
        binding.setVariable(BR.prefUtils, prefUtils)
        binding.setVariable(BR.fragment, this)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        if (context is BaseActivity<*, *>) {
            iBase = context
        }
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
        dialog?.apply {
            setOnShowListener {
                val bottomSheet = findViewById<View?>(com.google.android.material.R.id.design_bottom_sheet)
                bottomSheet?.setBackgroundResource(android.R.color.transparent)
            }
        }
        initAds()
        initView()
        initData()
        initListener()
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