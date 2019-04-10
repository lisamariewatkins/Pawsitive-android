package com.example.network.petlist

import com.example.network.RetrofitFactory
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

@Deprecated("Migration to V2 under way")
interface PetManager {
    @GET("pet.find")
    fun getPetListAsync(@Query("location") location: String, @Query("offset") offset: String?): Deferred<PetJsonResponse>
}

@Deprecated("Migration to V2 under way")
class PetManagerImpl(retrofitFactory: RetrofitFactory): PetManager {
    private val retrofitInstance = retrofitFactory.retrofit().create(PetManager::class.java)

    override fun getPetListAsync(location: String, offset: String?): Deferred<PetJsonResponse> {
        return retrofitInstance.getPetListAsync(location, offset)
    }
}

interface PetManagerV2 {

}