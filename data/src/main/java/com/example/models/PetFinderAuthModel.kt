package com.example.models

import com.squareup.moshi.Json

data class PetFinderAuthResponse(@field:Json(name = "token_type") val tokenType: String,
                                 @field:Json(name = "expires_in") val expiresIn: String,
                                 @field:Json(name = "access_token") val accessToken: String)