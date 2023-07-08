package com.demo.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.demomoduleads.R
import com.demo.App
import com.demo.utils.NavigationUtils.safeNavigateAction
import com.demo.utils.NavigationUtils.safeNavigationUp
import com.demo.utils.NavigationUtils.safePopBackStack
import com.demo.utils.PrefUtils
import dagger.android.AndroidInjection
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import com.demo.ui.dialog.PopupExit
import com.demo.utils.LanguageUtils
import com.demomoduleads.BR
import com.demo.network.exception.NoConnectivityException
import com.demo.ui.activities.MainViewModel
import com.demo.ui.dialog.NoInternetPopup
import com.demo.utils.firebase.EventClick


open class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding> @Inject constructor(
    @LayoutRes val layout: Int, viewModelClass: Class<VM>
) : DaggerFragment(), IBaseUI {

    val TAG = this::class.java.name

    private var iBase: IBaseUI? = null
    val mainViewModel: MainViewModel by activityViewModels { viewModelFactory }

    @Inject
    lateinit var prefUtils: PrefUtils

    private var _binding: DB? = null

    open val binding get() = _binding!!

    var adsUtils = App.adsUtils

    open fun backPressedWithExitPopup(): Boolean = false

    open fun checkNoInternet(): Boolean = false

    open fun loadingText(): String = getString(R.string.loading)

    init {
        Log.d(TAG, "init: ${this::class.java.simpleName}")
    }

    open fun init(inflater: LayoutInflater, container: ViewGroup) {
        _binding = DataBindingUtil.inflate(inflater, layout, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.setVariable(BR.viewModel, viewModel)
        binding.setVariable(BR.prefUtils, prefUtils)
        binding.setVariable(BR.fragment, this)
    }

    open fun initView() {}

    open fun initData() {}

    open fun initAds() {}

    open fun clearAds() {}

    open fun initListener() {
        viewModel.isLoading.observe(this.viewLifecycleOwner) {
            if (it) {
                showLoading(loadingText())
            } else {
                hideLoading()
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[viewModelClass]
    }

    open fun onInject() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(activity)
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ${this::class.java.simpleName}")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView: ${this::class.java.simpleName}")
        LanguageUtils.getDefaultLanguage(requireContext())
        init(inflater, container!!)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAds()
        initView()
        initData()
        initListener()

        if (checkNoInternet()) {
            viewModel.error.observe(this.viewLifecycleOwner) {
                if (it.ex is NoConnectivityException) {
                    NoInternetPopup.showPopup(childFragmentManager) {
                        onTryAgainNoInternet()
                    }
                } else {
                    showToast(it.messagge)
                }
            }
        }

        if (backPressedWithExitPopup()) {
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    PopupExit().show(childFragmentManager, PopupExit::class.java.simpleName)
                }
            })
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*, *>) {
            iBase = context
        }
    }

    open fun onTryAgainNoInternet() {

    }

    fun safeNav(action: NavDirections) {
        safeNavigateAction(action)
    }

    fun safeBackNav() {
        safeNavigationUp()
    }

    fun safePopBackStackNav(id: Int, inclusive: Boolean, saveState: Boolean) {
        safePopBackStack(id, inclusive, saveState)
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        hideSystemNavigationBar()
    }

    open fun refresh() {}

    open fun goBack() {
        findNavController().popBackStack()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        clearAds()
    }
}