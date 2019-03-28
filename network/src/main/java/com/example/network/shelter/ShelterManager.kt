package com.example.network.shelter

import com.example.network.RetrofitFactory
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface ShelterManager {
    @GET("shelter.find")
    fun getShelterListAsync(@Query("location") location: String): Deferred<ShelterJsonResponse>
}

class ShelterManagerImpl: ShelterManager {
    private val retrofitInstance = RetrofitFactory.retrofit().create(ShelterManager::class.java)

    override fun getShelterListAsync(location: String): Deferred<ShelterJsonResponse> {
        return retrofitInstance.getShelterListAsync(location)
    }
}