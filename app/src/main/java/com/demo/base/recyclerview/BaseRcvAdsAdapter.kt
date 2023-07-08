package com.demo.base.recyclerview

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.demo.base.BaseData
import com.demo.utils.customview.recyclerview.AutoUpdatableAdapter

abstract class BaseRcvAdsAdapter<T : BaseData, B : ViewDataBinding, ADS : ViewDataBinding> :
    RecyclerView.Adapter<BaseRcvAdsAdapter<T, B, ADS>.RcvViewHolder>(), AutoUpdatableAdapter {

    val TAG = this::class.java.simpleName
    private val TYPE_ITEM = 1
    private val TYPE_ADS = 2
    var stepAds = 2

    var listItem = mutableListOf<T>()

    init {
        Log.d(TAG, "init: ${this::class.java.simpleName}")
    }

    abstract fun createHolder(parent: ViewGroup): BaseViewHolder

    abstract fun createAdsHolder(parent: ViewGroup): BaseAdsViewHolder

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): RcvViewHolder {
        return if (viewType == TYPE_ADS) {
            createAdsHolder(parent)
        } else {
            createHolder(parent)
        }
    }


    override fun getItemCount(): Int {
        return listItem.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (listItem[position].isShowAds) {
            TYPE_ADS
        } else {
            TYPE_ITEM
        }
    }

    override fun onBindViewHolder(holder: RcvViewHolder, position: Int) {
        when (holder) {
            is BaseViewHolder -> holder.onBind(listItem[position])
            is BaseAdsViewHolder -> holder.onBind(listItem[position])
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    open fun updateData(items: ArrayList<T>) {
        listItem = items
        notifyDataSetChanged()
    }

    fun getData() = listItem

    open inner class RcvViewHolder(open var binding: B?, open var bindingAds: ADS?) : RecyclerView.ViewHolder(binding?.root ?: bindingAds!!.root) {

        val context: Context
            get() = binding?.root?.context ?: bindingAds?.root?.context!!

        open fun onBind(data: T) {

        }
    }

    open inner class BaseViewHolder(override var binding: B?) : RcvViewHolder(binding, null) {


        override fun onBind(data: T) {
        }
    }

    open inner class BaseAdsViewHolder(override var bindingAds: ADS?) : RcvViewHolder(null, bindingAds) {

        override fun onBind(data: T) {
        }
    }
}