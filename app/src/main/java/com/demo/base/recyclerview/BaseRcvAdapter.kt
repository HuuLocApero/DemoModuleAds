package com.demo.base.recyclerview

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.demo.base.BaseData
import com.demo.utils.customview.recyclerview.AutoUpdatableAdapter

abstract class BaseRcvAdapter<T : BaseData, B : ViewDataBinding>() :
    RecyclerView.Adapter<BaseRcvAdapter<T, B>.BaseViewHolder>(), AutoUpdatableAdapter {

    val TAG = this::class.java.simpleName

    var listItem = mutableListOf<T>()

    init {
        Log.d(TAG, "init: ${this::class.java.simpleName}")
    }

    abstract fun createHolder(parent: ViewGroup): BaseViewHolder
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): BaseViewHolder {
        return createHolder(parent)
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(listItem[holder.absoluteAdapterPosition])
    }

    @SuppressLint("NotifyDataSetChanged")
    open fun updateData(items: ArrayList<T>) {
        listItem = items
        notifyDataSetChanged()
    }

    fun getData() = listItem

    open inner class BaseViewHolder(val binding: B) : RecyclerView.ViewHolder(binding.root) {
        var context: Context = binding.root.context

        open fun onBind(data: T) {
            binding
        }
    }
}