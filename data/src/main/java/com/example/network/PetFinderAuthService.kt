package com.example.network

import com.example.models.PetFinderAuthResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PetFinderAuthService {
    @POST("/v2/oauth2/token")
    @FormUrlEncoded
    fun refreshToken(
        @Field("grant_type") grantType: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String
    ): Call<PetFinderAuthResponse>
}
