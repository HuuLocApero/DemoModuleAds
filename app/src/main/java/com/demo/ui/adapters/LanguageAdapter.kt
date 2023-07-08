package com.demo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import com.demomoduleads.R
import com.demomoduleads.databinding.ItemLanguageBinding
import com.demo.base.recyclerview.BaseRcvAdapter
import com.demo.base.recyclerview.BaseRecyclerViewListener
import com.demo.common.Constant
import com.demo.data.ui.Language
import com.demo.utils.AppUtils.getDrawableCompat

class LanguageAdapter(var listener: BaseRecyclerViewListener<Language>? = null) : BaseRcvAdapter<Language, ItemLanguageBinding>() {

    private var isSelectedIndex: Int = 0

    override fun createHolder(parent: ViewGroup) = ViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.item_language, parent, false
        )
    )

    inner class ViewHolder(binding: ItemLanguageBinding) : BaseViewHolder(binding) {

        override fun onBind(data: Language) {
            with(binding) {
                val context = rootView.context
                val typeface = ResourcesCompat.getFont(
                    context, if (data.isChoose) R.font.exo2_bold else R.font.exo2_regular
                )
                tvName.typeface = typeface
                tvName.text = data.name

                ivCountry.setBackgroundResource(data.idIcon)
                if (data.isChoose) {
                    rootView.background = context.getDrawableCompat(R.drawable.bg_button)
                } else {
                    rootView.background = context.getDrawableCompat(R.drawable.bg_white_alpha_10_radius_50)
                }
                itemView.setOnClickListener {
                    listItem[isSelectedIndex].isChoose = false
                    listItem[absoluteAdapterPosition].isChoose = true
                    notifyItemChanged(isSelectedIndex)
                    notifyItemChanged(absoluteAdapterPosition)

                    isSelectedIndex = absoluteAdapterPosition

                    listener?.onClick(data, absoluteAdapterPosition)
                }
            }
        }
    }

    fun getSelectedLanguageCode(): String {
        listItem.forEach { lang ->
            if (lang.isChoose) {
                return lang.code
            }
        }
        return Constant.LANGUAGE_EN
    }
}