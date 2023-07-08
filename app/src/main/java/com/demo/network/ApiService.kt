package com.demo.network

import com.demo.network.api.base.BaseResponse
import com.demo.network.api.base.DataList
import com.demo.network.response.StyleItemResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    @GET(ApiConstant.STYLE_URL)
    @Headers("Content-Type: application/json")
    fun getListStyleAsync(
        @Query("limit") limit: Int = 10000,
    ): Deferred<Response<BaseResponse<DataList<StyleItemResponse>>>>
}