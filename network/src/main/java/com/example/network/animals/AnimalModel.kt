package com.example.network.animals

import com.squareup.moshi.Json

data class AnimalJsonResponse(@field:Json(name = "animals") val animals: List<Animal>)

data class Animal(@field:Json(name = "id") val id: Int,
                  @field:Json(name = "organization_id") val organizationId: String)