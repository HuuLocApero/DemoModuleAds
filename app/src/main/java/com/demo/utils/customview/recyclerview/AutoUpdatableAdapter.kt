package com.demo.utils.customview.recyclerview

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.demo.base.BaseData
import com.demo.base.recyclerview.BaseRcvAdapter
import com.demo.base.recyclerview.BaseRcvAdsAdapter

interface AutoUpdatableAdapter {

    fun <T : BaseData, B : ViewDataBinding> BaseRcvAdapter<T, B>.autoNotify(
        old: List<T>,
        new: List<T>,
        compare: (T, T) -> Boolean
    ) {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return compare(old[oldItemPosition], new[newItemPosition])
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return old[oldItemPosition] == new[newItemPosition]
            }

            override fun getOldListSize() = itemCount

            override fun getNewListSize() = new.size
        })
        listItem.clear()
        listItem = new.toMutableList()
        diff.dispatchUpdatesTo(this)
    }

    fun <T : BaseData, B : ViewDataBinding, ADS : ViewDataBinding> BaseRcvAdsAdapter<T, B, ADS>.autoNotify(
        old: List<T>,
        new: List<T>,
        compare: (T, T) -> Boolean
    ) {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return compare(old[oldItemPosition], new[newItemPosition])
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return old[oldItemPosition] == new[newItemPosition]
            }

            override fun getOldListSize() = itemCount

            override fun getNewListSize() = new.size
        })
        listItem.clear()
        listItem = new.toMutableList()
        diff.dispatchUpdatesTo(this)
    }
}