package com.example.network.petlist

import com.example.network.RetrofitFactory
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface PetManager {
    @GET("pet.find")
    fun getPetListAsync(@Query("location") location: String, @Query("offset") offset: String?): Deferred<PetJsonResponse>
}

class PetManagerImpl(retrofitFactory: RetrofitFactory): PetManager {
    private val retrofitInstance = retrofitFactory.retrofit().create(PetManager::class.java)

    override fun getPetListAsync(location: String, offset: String?): Deferred<PetJsonResponse> {
        return retrofitInstance.getPetListAsync(location, offset)
    }
}