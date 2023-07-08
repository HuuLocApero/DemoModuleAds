package com.demo.network.interceptor

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.demo.network.exception.NoConnectivityException
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException
import javax.inject.Inject

class NetworkConnectionInterceptor @Inject constructor(val context: Context) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isConnected()) {
            throw NoConnectivityException()
        }

        val url = chain.request().url.newBuilder().build()
        val requestBuilder = chain.request().newBuilder()
//        requestBuilder.addHeader(key, value)
        val request = requestBuilder.url(url).build()
        return chain.proceed(request)
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork
        if (network != null) {
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            if (networkCapabilities != null) {
                return (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || networkCapabilities.hasTransport(
                    NetworkCapabilities.TRANSPORT_WIFI
                ))
            }
        }
        return false
    }
}