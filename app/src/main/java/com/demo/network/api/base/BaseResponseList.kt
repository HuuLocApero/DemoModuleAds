package com.demo.network.api.base

import com.google.gson.annotations.SerializedName

data class BaseResponseList<T>(
    @SerializedName("data") var data: DataList<T>? = null
)

data class DataList<T>(
    @SerializedName("items") var items: ArrayList<T>? = null
)
