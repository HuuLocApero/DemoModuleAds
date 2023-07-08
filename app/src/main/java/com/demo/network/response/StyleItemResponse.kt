package com.demo.network.response

import com.google.gson.annotations.SerializedName

data class StyleItemResponse(
    @SerializedName("_id") var id: String? = null,
    @SerializedName("name") var nameStyle: String? = null,
    @SerializedName("prompt") var prompt: String? = null,
    @SerializedName("key") var key: String? = null,
    @SerializedName("config") var config: StyleConfigResponse? = null
)


data class StyleConfigResponse(
    @SerializedName("cfgScale") var cfgScale: Double? = null,
    @SerializedName("steps") var steps: Double? = null,
    @SerializedName("stepScheduleStart") var stepScheduleStart: Double? = null,
    @SerializedName("algorithym") var algorithym: String? = null,
    @SerializedName("positivePrompt") var positivePrompt: String? = null,
    @SerializedName("negativePrompt") var negativePrompt: String? = null
)