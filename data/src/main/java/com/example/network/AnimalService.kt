package com.example.network

import com.example.models.AnimalJsonResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface AnimalService {
    @GET("/v2/animals")
    fun getAnimalsAsync(@Query("page") page: Int = 1): Deferred<AnimalJsonResponse>
}

class AnimalServiceImpl(retrofitFactory: RetrofitFactoryV2): AnimalService {
    private val retrofitInstance = retrofitFactory.retrofit().create(AnimalService::class.java)

    override fun getAnimalsAsync(page: Int): Deferred<AnimalJsonResponse> {
        return retrofitInstance.getAnimalsAsync(page)
    }
}