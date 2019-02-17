package com.example.network.petlist

import com.example.network.RetrofitFactory
import com.example.network.secret.Key
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PetManager {
    @GET
    fun getPetList(@Query("location") location: String): Call<PetRecordList>
}

class PetManagerImpl: PetManager {
    val retrofitInstance = RetrofitFactory.retrofit().create(PetManager::class.java)

    override fun getPetList(location: String): Call<PetRecordList> {
        return retrofitInstance.getPetList(location)
    }

}