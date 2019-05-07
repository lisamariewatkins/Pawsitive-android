package com.example.network

import com.example.models.AnimalJsonResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface AnimalService {
    @GET("/v2/animals")
    fun getAnimalsAsync(): Deferred<AnimalJsonResponse>
}

class AnimalServiceImpl(retrofitFactory: RetrofitFactoryV2): AnimalService {
    private val retrofitInstance = retrofitFactory.retrofit().create(AnimalService::class.java)

    override fun getAnimalsAsync(): Deferred<AnimalJsonResponse> {
        return retrofitInstance.getAnimalsAsync()
    }
}