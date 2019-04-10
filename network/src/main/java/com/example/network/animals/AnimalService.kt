package com.example.network.animals

import com.example.network.RetrofitFactoryV2
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface AnimalService {
    @GET("animals")
    fun getAnimalsAsync(): Deferred<AnimalJsonResponse>
}

class AnimalServiceImpl(retrofitFactory: RetrofitFactoryV2): AnimalService {
    private val retrofitInstance = retrofitFactory.retrofit().create(AnimalService::class.java)

    override fun getAnimalsAsync(): Deferred<AnimalJsonResponse> {
        return retrofitInstance.getAnimalsAsync()
    }
}