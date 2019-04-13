package com.example.network

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.network.petfinderauth.PetFinderAuthService
import com.example.network.secret.Key
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import okhttp3.OkHttpClient

const val BASE_URL = "https://api.petfinder.com/"
const val BASE_URL_V2 = "https://api.petfinder.com"
const val AUTHORIZATION = "Authorization"
const val CLIENT_CREDENTIALS = "client_credentials"
const val KEY = "key"
const val FORMAT = "format"
const val JSON = "json"
const val ACCESS_TOKEN = "access_token"

@Deprecated("Migration to V2 in progress")
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

/**
 * Creates retrofit instance for use with all PetFinder API calls
 */
class RetrofitFactoryV2(val context: Context) {
    private val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val tokenAuthenticator = TokenAuthenticator(retrofitRefresh(), context)

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(tokenAuthenticator)
        .authenticator(tokenAuthenticator)

    /**
     * Retrofit instance for all PetFinder API calls
     */
    fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(BASE_URL_V2)
            .client(okHttpClient.build())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    /**
     * Retrofit instance for auth token refresh
     */
    private fun retrofitRefresh(): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)

        return Retrofit.Builder()
            .baseUrl(BASE_URL_V2)
            .client(okHttpClient.build())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }
}

/**
 * Intercept requests and add authorization token
 */
class TokenAuthenticator(private val retrofit: Retrofit,
                         context: Context): Authenticator, Interceptor {

    private val sharedPref = context.getSharedPreferences(ACCESS_TOKEN, MODE_PRIVATE)
    private var accessToken: String? = sharedPref.getString(ACCESS_TOKEN, null)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader(AUTHORIZATION, "Bearer $accessToken")
            .build()

        return chain.proceed(request)
    }

    /**
     * Okhttp will automatically ask the [Authenticator] for credentials when the response is 401 Not Authorized and retry
     * the last failed request with the new credentials
     */
    override fun authenticate(route: Route?, response: Response): Request? {
        val petFinderAuthService = retrofit.create(PetFinderAuthService::class.java)
        val retrofitResponse = petFinderAuthService.refreshToken(CLIENT_CREDENTIALS, Key.API_KEY_V2, Key.API_SECRET_V2)

        accessToken = retrofitResponse.execute().body()?.accessToken
        sharedPref.edit().putString(ACCESS_TOKEN, accessToken).apply()

        return if (accessToken != null) {
            // Add new header to rejected request and retry it
            response.request().newBuilder()
                .header(AUTHORIZATION, "Bearer $accessToken")
                .build()
        } else {
            null
        }
    }
}
