package com.example.network.shelter

import com.squareup.moshi.Json

data class ShelterJsonResponse(@field:Json(name = "@version") val version: String,
                        @field:Json(name = "petfinder") val petFinder: PetFinder)

data class PetFinder(@field:Json(name = "lastOffset") val lastOffset: KeyValuePair,
                     @field:Json(name = "shelters") val shelters: ShelterObject)

data class ShelterObject(@field:Json(name = "shelter") val shelterList: List<Shelter>)

data class Shelter(
//    @field:Json(name = "options") val options: Options, todo fix this
    @field:Json(name = "country") val country: KeyValuePair,
    @field:Json(name = "longitude") val longitude: KeyValuePair,
    @field:Json(name = "name") val name: KeyValuePair,
    @field:Json(name = "phone") val phone: KeyValuePair,
    @field:Json(name = "state") val state: KeyValuePair,
    @field:Json(name = "address2") val address2: KeyValuePair,
    @field:Json(name = "email") val email: KeyValuePair,
    @field:Json(name = "city") val city: KeyValuePair,
    @field:Json(name = "zip") val zip: KeyValuePair,
    @field:Json(name = "fax") val fax: KeyValuePair,
    @field:Json(name = "latitude") val latitude: KeyValuePair,
    @field:Json(name = "id") val id: KeyValuePair,
    @field:Json(name = "address1") val address1: KeyValuePair)

data class KeyValuePair(@field:Json(name = "\$t") val value: String)