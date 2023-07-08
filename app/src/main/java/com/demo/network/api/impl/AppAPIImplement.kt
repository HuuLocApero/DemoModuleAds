package com.demo.network.api.impl

import com.demo.data.repository.base.BaseRepository
import com.demo.network.ApiService
import com.demo.network.api.AppAPI
import com.demo.network.response.StyleItemResponse
import javax.inject.Inject

class AppAPIImplement @Inject constructor(private val apiService: ApiService) : AppAPI, BaseRepository<StyleItemResponse>(apiService) {

}