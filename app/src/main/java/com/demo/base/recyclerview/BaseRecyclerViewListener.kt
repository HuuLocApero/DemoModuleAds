package com.demo.base.recyclerview

import com.demo.base.BaseData


interface BaseRecyclerViewListener<T : BaseData> {
    fun onClick(data: T, position: Int) {

    }
}