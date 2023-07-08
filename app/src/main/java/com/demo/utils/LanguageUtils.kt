package com.demo.utils

import android.content.Context
import android.content.res.Configuration
import com.demomoduleads.R
import com.demo.common.Constant
import com.demo.data.ui.Language
import java.util.Locale

class LanguageUtils {
    companion object {
        fun changeDefaultLanguage(context: Context, language: String) {/*save in shared preferences*/
            PrefUtils.instant.currentLanguage = language

            /*convert language codeName to locale-format*/
            val locale = Locale(language)
            Locale.setDefault(locale)


            /*set language for the application*/
            val config = Configuration()
            config.setLocale(locale)


            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            context.applicationContext.resources.updateConfiguration(
                config, context.applicationContext.resources.displayMetrics
            )
        }

        fun getDefaultLanguage(context: Context) {
            val language: String = PrefUtils.instant.currentLanguage

            if (language == "") {
                val config = Configuration()
                val locale = Locale.getDefault()
                Locale.setDefault(locale)
                config.setLocale(locale)


                context.resources.updateConfiguration(config, context.resources.displayMetrics)
            } else {
                if (language == Constant.CODENAME_LANGUAGE_ENGLISH) {
                    changeDefaultLanguage(context, Constant.CODENAME_LANGUAGE_ENGLISH)
                } else {
                    changeDefaultLanguage(context, language)
                }
            }
        }/*end fun getDefaultLanguage*/

        fun languageListItems(context: Context, defaultLanguageCode: String = ""): ArrayList<Language> {
            val listLanguage = arrayListOf(
                Language(
                    Constant.LANGUAGE_EN, context.getString(R.string.english), com.ltl.apero.languageopen.R.drawable.ic_language_en, false
                ), Language(
                    Constant.LANGUAGE_ES, context.getString(R.string.spanish), com.ltl.apero.languageopen.R.drawable.ic_language_es, false
                ), Language(
                    Constant.LANGUAGE_PT, context.getString(R.string.portugal), com.ltl.apero.languageopen.R.drawable.ic_language_pt, false
                ), Language(
                    Constant.LANGUAGE_KO, context.getString(R.string.korean), com.ltl.apero.languageopen.R.drawable.ic_language_ko, false
                ), Language(
                    Constant.LANGUAGE_INDO, context.getString(R.string.indonesian), com.ltl.apero.languageopen.R.drawable.ic_language_indo, false
                ), Language(
                    Constant.LANGUAGE_HINDI, context.getString(R.string.hindi), com.ltl.apero.languageopen.R.drawable.ic_language_hi, false
                )
            )

            if (defaultLanguageCode.isNotEmpty()) {
                var deviceLanguage: Language? = null
                listLanguage.find { language -> language.code == context.resources.configuration.locale.language }?.also {
                    deviceLanguage = it
                }

                if (deviceLanguage != null) {
                    listLanguage.remove(deviceLanguage)
                    listLanguage.add(0, deviceLanguage!!)
                }

                listLanguage.forEachIndexed { index, language ->
                    language.isChoose = language.code == defaultLanguageCode
                }
            }
            return listLanguage
        }

    }
}