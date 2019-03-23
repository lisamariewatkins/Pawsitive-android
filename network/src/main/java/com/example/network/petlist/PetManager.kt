package com.example.network.petlist

import com.example.network.RetrofitFactory
import com.example.network.secret.Key
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PetManager {
    @GET("pet.find")
    fun getPetListAsync(@Query("location") location: String, @Query("offset") offset: String?): Deferred<JsonResponse>
}

class PetManagerImpl: PetManager {
    private val retrofitInstance = RetrofitFactory.retrofit().create(PetManager::class.java)

    override fun getPetListAsync(location: String, offset: String?): Deferred<JsonResponse> {
        return retrofitInstance.getPetListAsync(location, offset)
    }
}