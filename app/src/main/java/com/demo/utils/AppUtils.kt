package com.demo.utils

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.provider.Settings
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.ads.control.admob.AppOpenManager
import com.demomoduleads.R
import com.demo.base.BaseViewModel
import java.lang.Exception

object AppUtils {

    fun <T : ViewDataBinding> Fragment.getDataBinding(inflater: LayoutInflater, @LayoutRes layoutId: Int, container: ViewGroup?): T =
        DataBindingUtil.inflate(inflater, layoutId, container, false)

    inline fun <reified T : BaseViewModel> Fragment.getViewModel(factory: ViewModelProvider.Factory = ViewModelProviders.DefaultFactory(activity!!.application)): T =
        ViewModelProviders.of(this, factory)[T::class.java]

    val Int.dp: Int
        get() = (toFloat() * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

    fun dpToPx(dp: Int, context: Context): Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics
    ).toInt()

    val Number.toPx
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics
        )

    fun pxToDp(px: Float) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, px, Resources.getSystem().displayMetrics
    )

    internal fun Context.getDrawableCompat(@DrawableRes drawable: Int) = ContextCompat.getDrawable(this, drawable)

    internal fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

    fun isNetworkConnected(context: Context?): Boolean {
        if (context != null) {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
        return false
    }

    fun openWebsite(context: Context, url: String) {
        try {
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            val intentChooser = Intent.createChooser(intent, "Select your browser to continue")
            context.startActivity(intentChooser)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun onOpenSettingPermissionReadStorage(context: Context) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setMessage(R.string.request_permission)
        builder.setPositiveButton(context.getString(R.string.yes)) { _, _ ->
            AppOpenManager.getInstance().enableAppResume()
            AppOpenManager.getInstance().disableAdResumeByClickAction()
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", context.packageName, null)
            intent.data = uri
            context.startActivity(intent)
        }
        builder.setNegativeButton(R.string.no) { dialog, _ ->
            AppOpenManager.getInstance().enableAppResume()
            dialog.dismiss()
        }
        builder.show()
    }
}