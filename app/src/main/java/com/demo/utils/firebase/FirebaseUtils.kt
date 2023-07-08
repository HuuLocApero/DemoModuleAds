package com.demo.utils.firebase

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import java.lang.Exception

object FirebaseUtils {
    private val TAG = FirebaseUtils::class.java.simpleName
    private var fbAnalytics: FirebaseAnalytics? = null

    fun init(context: Context?) {
        fbAnalytics = FirebaseAnalytics.getInstance(context!!)
    }

    fun trackEvent(event: EventClick, param: String?, value: String?) {
        try {
            Log.d(TAG, "trackEvent: ${event.content}")
            val bundle: Bundle? = if (!param.isNullOrEmpty() && !value.isNullOrEmpty()) {
                bundleOf(param to value)
            } else {
                null
            }
            fbAnalytics?.logEvent(event.name, bundle)
        } catch (_: Exception) {

        }
    }

    fun trackEvent(event: String, param: String?, value: String?) {
        try {
            Log.d(TAG, "trackEvent: ${event}")
            val bundle: Bundle? = if (param.isNullOrEmpty() && value.isNullOrEmpty()) {
                bundleOf(param!! to value!!)
            } else {
                null
            }
            fbAnalytics?.logEvent(event, bundle)
        } catch (_: Exception) {

        }
    }

    fun eventImpressionAds(adsName: String, idAds: String) {
        val name = try {
            "${adsName}_impression_${idAds.subSequence(idAds.length - 4, idAds.length)}"
        } catch (ex: Exception) {
            "${adsName}_impression"
        }
        Log.d(TAG, name)
        fbAnalytics?.logEvent(name, null)
    }

    fun eventMustDisplayAds(adsName: String) {
        Log.d(TAG, adsName)
        fbAnalytics?.logEvent(adsName, null)
    }

    fun eventDisplayAds(adsName: String, idAds: String) {
        val name = try {
            "${adsName}_display_${idAds.subSequence(idAds.length - 4, idAds.length)}"
        } catch (ex: Exception) {
            "${adsName}_display"
        }
        Log.d(TAG, name)
        fbAnalytics?.logEvent(name, null)
    }
}