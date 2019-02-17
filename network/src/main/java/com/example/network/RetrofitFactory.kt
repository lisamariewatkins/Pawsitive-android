package com.example.network

import com.example.network.secret.Key
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "http://api.petfinder.com/";
const val KEY = "key"
const val FORMAT = "format"
const val JSON = "json"

class RetrofitFactory {
    companion object {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(
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
                .baseUrl(BASE_URL)
                .client(okHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}