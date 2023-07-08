@file:Suppress("DEPRECATION")

package com.demo.base

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Rect
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.ads.control.admob.AppOpenManager
import com.demo.App
import com.demo.ui.dialog.LoadingDialog
import com.demo.utils.LanguageUtils
import com.demo.utils.PrefUtils
import com.demo.utils.firebase.EventClick
import com.demo.utils.firebase.FirebaseUtils
import com.ironsource.mediationsdk.IronSource
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


open class BaseActivity<VM : BaseViewModel, VB : ViewBinding> @Inject constructor(
    @LayoutRes val layoutId: Int, private val mViewModelClass: Class<VM>
) : DaggerAppCompatActivity(), IBaseUI {

    var isOnResume: Boolean = false
    private var loadingDialog: LoadingDialog? = null

    @Inject
    lateinit var prefUtils: PrefUtils
    var adsUtils = App.adsUtils
    private val isFinished: Boolean
        get() {
            if (isDestroyed || isFinishing) {
                return true
            }
            return false
        }

    lateinit var binding: VB

    @Inject
    internal lateinit var viewModelProviderFactory: ViewModelProvider.Factory

    val viewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory)[mViewModelClass]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId) as VB
        setContentView(binding.root)
    }

    override fun showLoading(content: String) {
        if (!isFinished && (loadingDialog == null || loadingDialog?.dialog?.isShowing == false)) {
            loadingDialog = LoadingDialog()
            loadingDialog?.show(supportFragmentManager, "loading")
        }
    }

    override fun hideLoading() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    override fun isNetworkConnected(): Boolean {
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return cm?.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }

    override fun hideSystemNavigationBar() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                run {
                    val windowInsetController = ViewCompat.getWindowInsetsController(window.decorView)
                    if (windowInsetController != null) {
                        windowInsetController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                        windowInsetController.hide(WindowInsetsCompat.Type.navigationBars())
                        if (window.decorView.rootWindowInsets != null) {
                            window.decorView.rootWindowInsets.getInsetsIgnoringVisibility(
                                WindowInsetsCompat.Type.navigationBars()
                            )
                        }
                        window.setDecorFitsSystemWindows(true)
                    }
                }
            } else {
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun showToast(content: String) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
    }

    override fun hideKeyboard(view: View?) {
        val inputMethodManager = view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun disableAdsResume() {
        AppOpenManager.getInstance().disableAppResume()
    }

    override fun disableAdsResumeByClickAction() {
        AppOpenManager.getInstance().disableAdResumeByClickAction()
    }

    override fun enableAdsResume() {
        AppOpenManager.getInstance().enableAppResume()
    }

    override fun trackEvent(event: EventClick, param: String?, value: String?) {
        FirebaseUtils.trackEvent(event, param, value)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    Log.d("focus", "touchevent")
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        hideSystemNavigationBar()
    }

    override fun onPause() {
        super.onPause()
        isOnResume = false
        IronSource.onPause(this)
    }

    override fun onResume() {
        super.onResume()
        IronSource.onResume(this)
        isOnResume = true
        LanguageUtils.getDefaultLanguage(this)
    }
}