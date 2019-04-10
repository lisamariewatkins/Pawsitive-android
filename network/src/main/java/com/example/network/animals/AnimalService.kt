package com.example.network.animals

import com.example.network.RetrofitFactoryV2
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface AnimalService {
    @GET("animals")
    fun getAnimalsAsync(@Query("location") location: String): Deferred<AnimalJsonResponse>
}

class AnimalServiceImpl(retrofitFactory: RetrofitFactoryV2): AnimalService {
    private val retrofitInstance = retrofitFactory.retrofit().create(AnimalService::class.java)

    override fun getAnimalsAsync(location: String): Deferred<AnimalJsonResponse> {
        return retrofitInstance.getAnimalsAsync(location)
    }
}