package com.demo.data.repository.base

import android.util.Log
import com.demo.network.ApiService
import com.demo.network.api.base.BaseResponse
import com.demo.network.exception.ForbiddenException
import com.demo.network.exception.NoConnectivityException
import com.demo.network.exception.ServerNotFoundException
import com.demo.network.exception.UnauthorizedException
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.lang.Exception

abstract class BaseRepository<T>(@PublishedApi internal val service: ApiService) {
    val TAG = this::class.java.simpleName

    suspend inline fun <reified T : Any> fetchData(crossinline call: (ApiService) -> Deferred<Response<BaseResponse<T>>>): T {
        var result: T? = null

        val request = call(service)
        withContext(Dispatchers.Main) {
            try {
                val response = request.await()
                if (response.isSuccessful) {
                    result = response.body()?.data!!
                } else {
                    Log.d(TAG, "Error occurred with code ${response.code()}")
                    when (response.code()) {
                        401 -> throw UnauthorizedException()
                        403 -> throw ServerNotFoundException()
                        404 -> throw ForbiddenException()
                        else -> throw Exception()
                    }
                }
            } catch (e: NoConnectivityException) {
                throw NoConnectivityException()
            } catch (e: Throwable) {
                Log.d(TAG, "Error: ${e.message}")
                throw Exception(e)
            }
        }

        return result ?: throw Exception()
    }

    suspend inline fun <reified T : Any> fetchDataBody(crossinline call: (ApiService) -> Deferred<Response<T>>): T {
        var result: T? = null

        val request = call(service)
        withContext(Dispatchers.Main) {
            try {
                val response = request.await()
                if (response.isSuccessful) {
                    result = response.body()
                } else {
                    Log.d(TAG, "Error occurred with code ${response.code()}")
                    when (response.code()) {
                        401 -> throw UnauthorizedException()
                        403 -> throw ServerNotFoundException()
                        404 -> throw ForbiddenException()
                        else -> throw Exception()
                    }
                }
            } catch (e: NoConnectivityException) {
                throw NoConnectivityException()
            } catch (e: Throwable) {
                Log.d(TAG, "Error: ${e.message}")
                throw Exception(e)
            }
        }

        return result ?: throw Exception()
    }


//    inline fun <reified T : Any> saveToDatabase(data: List<T>) {
//        CoroutineScope(Dispatchers.IO).launch {
//            boxStore.boxFor<T>().removeAll()  // deleting and inserting data to avoid sync issues
//            boxStore.boxFor<T>().put(data)
//        }
//    }
}