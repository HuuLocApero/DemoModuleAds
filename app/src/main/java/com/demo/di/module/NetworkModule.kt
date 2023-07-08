package com.demo.di.module

import android.content.Context
import android.os.Environment
import com.demomoduleads.BuildConfig
import com.demo.common.Constant.TIME_OUT_API
import com.demo.network.ApiService
import com.demo.network.api.AppAPI
import com.demo.network.api.impl.AppAPIImplement
import com.demo.network.interceptor.NetworkConnectionInterceptor
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {

    private val logger: HttpLoggingInterceptor
        get() {
            val loggingInterceptor = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                loggingInterceptor.apply { level = HttpLoggingInterceptor.Level.BODY }
            }
            return loggingInterceptor
        }

    @Singleton
    @Provides
    @Named("cached")
    fun provideOkHttpClient(context: Context): OkHttpClient {
        val cache = Cache(Environment.getDownloadCacheDirectory(), 10 * 1024 * 1024)
        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .addInterceptor(NetworkConnectionInterceptor(context))
            .readTimeout(TIME_OUT_API, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT_API, TimeUnit.SECONDS)
            .connectTimeout(TIME_OUT_API, TimeUnit.SECONDS)
            .cache(cache)
            .build()
    }

    @Singleton
    @Provides
    @Named("non_cached")
    fun provideNonCachedOkHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .addInterceptor(NetworkConnectionInterceptor(context))
            .readTimeout(TIME_OUT_API, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT_API, TimeUnit.SECONDS)
            .connectTimeout(TIME_OUT_API, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(@Named("non_cached") client: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit.Builder): ApiService {
        return retrofit.baseUrl(BuildConfig.BASE_URL)
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAppAPI(apiService: ApiService): AppAPI {
        return AppAPIImplement(apiService)
    }
}