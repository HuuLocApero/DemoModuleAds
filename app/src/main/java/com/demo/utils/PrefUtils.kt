package com.demo.utils

import android.content.SharedPreferences
import com.demo.common.Constant
import javax.inject.Inject

class PrefUtils @Inject constructor(private val sharedPreferences: SharedPreferences) {

    init {
        instant = this
    }

    companion object {
        lateinit var instant: PrefUtils

        // Ket local
        const val KEY_LANGUAGE_FIRST_OPEN = "KEY_LANGUAGE_FIRST_OPEN"
        const val KEY_ONBOARDING_FIRST_OPEN = "KEY_ONBOARDING_FIRST_OPEN"
        const val KEY_CURRENT_LANGUAGE = "KEY_CURRENT_LANGUAGE"
        const val KEY_LOADED_STYLE_IN_SPLASH = "KEY_LOADED_STYLE_IN_SPLASH"
        const val KEY_FAVOURITE_LIST = "KEY_FAVOURITE_LIST"

        // Key remote
        const val BANNER = "banner"
        const val INTER = "inter"
        const val NATIVE = "native"
        const val REWARD = "reward"
    }

    var banner: Boolean
        get() = sharedPreferences.getBoolean(BANNER, true)
        set(value) {
            sharedPreferences.edit()?.putBoolean(BANNER, value)?.apply()
        }

    var inter: Boolean
        get() = sharedPreferences.getBoolean(INTER, true)
        set(value) {
            sharedPreferences.edit()?.putBoolean(INTER, value)?.apply()
        }

    var native: Boolean
        get() = sharedPreferences.getBoolean(NATIVE, true)
        set(value) {
            sharedPreferences.edit()?.putBoolean(NATIVE, value)?.apply()
        }

    var reward: Boolean
        get() = sharedPreferences.getBoolean(REWARD, true)
        set(value) {
            sharedPreferences.edit()?.putBoolean(REWARD, value)?.apply()
        }

    var isShowLanguageFirstOpen: Boolean
        get() = sharedPreferences.getBoolean(KEY_LANGUAGE_FIRST_OPEN, true)
        set(value) {
            sharedPreferences.edit()?.putBoolean(KEY_LANGUAGE_FIRST_OPEN, value)?.apply()
        }

    var isShowOnBoardingFirstOpen: Boolean
        get() = sharedPreferences.getBoolean(KEY_ONBOARDING_FIRST_OPEN, true)
        set(value) {
            sharedPreferences.edit()?.putBoolean(KEY_ONBOARDING_FIRST_OPEN, value)?.apply()
        }

    var currentLanguage: String
        get() = sharedPreferences.getString(KEY_CURRENT_LANGUAGE, Constant.LANGUAGE_EN) ?: Constant.LANGUAGE_EN
        set(value) {
            sharedPreferences.edit()?.putString(KEY_CURRENT_LANGUAGE, value)?.apply()
        }

    fun resetData() {

    }
}