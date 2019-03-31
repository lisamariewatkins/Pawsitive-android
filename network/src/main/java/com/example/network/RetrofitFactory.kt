package com.example.network

import android.content.Context
import com.example.network.secret.Key
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

const val BASE_URL = "https://api.petfinder.com/";
const val KEY = "key"
const val FORMAT = "format"
const val JSON = "json"

class RetrofitFactory(val context: Context) {
    private val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB
    private val cache = Cache(context.cacheDir, cacheSize)

    private val okHttpClient = OkHttpClient.Builder()
        .cache(cache)
        .addInterceptor(
            // Query Interceptor
            object: Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val original = chain.request()
                    val originalUrl = original.url()

                    val new = originalUrl.newBuilder()
                        .addQueryParameter(KEY, Key.API_KEY)
                        .addQueryParameter(FORMAT, JSON)
                        .build()
                    val request = original.newBuilder()
                        .url(new)
                        .build()

                    return chain.proceed(request)
                }
            }
        )

    fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(BASE_URL)
            .client(okHttpClient.build())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }
}