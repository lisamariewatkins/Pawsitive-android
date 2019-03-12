package com.example.network.petlist

import com.example.network.RetrofitFactory
import com.example.network.secret.Key
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PetManager {
    @GET("pet.find")
    fun getPetList(@Query("location") location: String): Deferred<JsonResponse>
}

class PetManagerImpl: PetManager {
    private val retrofitInstance = RetrofitFactory.retrofit().create(PetManager::class.java)

    override fun getPetList(location: String): Deferred<JsonResponse> {
        return retrofitInstance.getPetList(location)
    }
}