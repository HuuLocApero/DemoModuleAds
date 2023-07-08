package com.demo.ui.fragments.language

import android.content.Intent
import android.view.View
import androidx.navigation.fragment.navArgs
import com.demomoduleads.R
import com.demomoduleads.databinding.FragmentLanguageBinding
import com.demo.base.BaseFragment
import com.demo.base.BaseViewModel
import com.demo.base.recyclerview.BaseRecyclerViewListener
import com.demo.common.Constant
import com.demo.data.ui.Language
import com.demo.ui.adapters.LanguageAdapter
import com.demo.utils.LanguageUtils
import com.demo.utils.customview.HeaderView
import com.demo.utils.firebase.EventClick

class LanguageFragment : BaseFragment<BaseViewModel, FragmentLanguageBinding>(
    R.layout.fragment_language, BaseViewModel::class.java
), HeaderView.Listener, BaseRecyclerViewListener<Language> {

    val args: LanguageFragmentArgs by navArgs()
    private val adapter = LanguageAdapter(this)

    override fun initAds() {
        adsUtils.nativeLanguage.showAds(requireActivity(), binding.flAds, false)
    }

    override fun clearAds() {
        adsUtils.nativeLanguage.clearAds()
    }

    override fun initView() {
        trackEvent(EventClick.SCR_LANGUAGE)
        binding.run {
            adapter.updateData(LanguageUtils.languageListItems(requireContext(), if (args.isFromSetting) prefUtils!!.currentLanguage else ""))
            rcvLanguage.adapter = adapter
        }
    }

    override fun onHeaderBackPressed() {
        safeBackNav()
    }

    override fun onOptionPressed(view: View) {
        val selectedLanguage = getSelectedLanguage()

        prefUtils.currentLanguage = selectedLanguage
        prefUtils.isShowLanguageFirstOpen = false

        if (args.isFromSetting) {
            val intent = requireActivity().intent
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
            intent.putExtra(Constant.KEY_START_FROM_LANGUAGE, true)
            requireActivity().finish()
            startActivity(intent)
        } else {
            trackEvent(EventClick.CLICK_NEXT_SCR_LANGUAGE, "language_code", selectedLanguage)
            LanguageUtils.changeDefaultLanguage(requireContext(), selectedLanguage)
            val action = LanguageFragmentDirections.actionLanguageFragmentToOnBoardingFragment()
            safeNav(action)
        }
    }

    override fun onClick(data: Language, position: Int) {
        binding.header.showOptionButton(true)
    }

    private fun getSelectedLanguage(): String {
        return try {
            (binding.rcvLanguage.adapter as LanguageAdapter).getSelectedLanguageCode()
        } catch (ex: Exception) {
            Constant.LANGUAGE_EN
        }
    }
}