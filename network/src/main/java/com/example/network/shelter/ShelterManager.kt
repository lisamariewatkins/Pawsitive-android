package com.example.network.shelter

import com.example.network.RetrofitFactory
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ShelterManager {
    @GET("shelter.find")
    fun getShelterListAsync(@Query("location") location: String,
                            @Header("Cache-Control") cacheControl: String?): Deferred<ShelterJsonResponse>
}

class ShelterManagerImpl(retrofitFactory: RetrofitFactory): ShelterManager {
    private val retrofitInstance = retrofitFactory.retrofit().create(ShelterManager::class.java)

    override fun getShelterListAsync(location: String, cacheControl: String?): Deferred<ShelterJsonResponse> {
        return retrofitInstance.getShelterListAsync(location, cacheControl)
    }
}