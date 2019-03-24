package com.example.network.petlist

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

data class JsonResponse(@field:Json(name = "@version") val version: String,
                        @field:Json(name = "petfinder") val petFinder: PetFinder)

data class PetFinder(@field:Json(name = "lastOffset") val lastOffset: KeyValuePair,
                    @field:Json(name = "pets") val pets: PetObject)

data class PetObject(@field:Json(name = "pet") val pet: List<Pet>)

data class Pet(
//    @field:Json(name = "options") val options: Options, todo fix this
               @field:Json(name = "status") val status: KeyValuePair,
               @field:Json(name = "contact") val contact: Contact,
               @field:Json(name = "age") val age: KeyValuePair,
               @field:Json(name = "size") val size: KeyValuePair,
               @field:Json(name = "media") val media: Photos,
               @field:Json(name = "id") val id: KeyValuePair,
               @field:Json(name = "name") val name: KeyValuePair,
               @field:Json(name = "sex") val sex: KeyValuePair,
               @field:Json(name = "description") val description: KeyValuePair,
               @field:Json(name = "mix") val mix: KeyValuePair,
               @field:Json(name = "shelterId") val shelterId: KeyValuePair,
               @field:Json(name = "lastUpdate") val lastUpdate: KeyValuePair,
               @field:Json(name = "animal") val animal: KeyValuePair)

data class Options(@field:Json(name = "option") val status: List<KeyValuePair>) // todo fix this

data class Photos(@field:Json(name = "photos") val photos: PhotoList?)

data class PhotoList(@field:Json(name= "photo") val photoList: List<PhotoInfo>)

data class PhotoInfo(@field:Json(name = "@size") val size: String,
                     @field:Json(name = "\$t") val url: String,
                     @field:Json(name = "@id") val id: String)

data class Contact(@field:Json(name = "phone") val phone: KeyValuePair,
                   @field:Json(name = "state") val state: KeyValuePair,
                   @field:Json(name = "address2") val address2: KeyValuePair,
                   @field:Json(name = "email") val email: KeyValuePair,
                   @field:Json(name = "city") val city: KeyValuePair,
                   @field:Json(name = "zip") val zip: KeyValuePair,
                   @field:Json(name = "fax") val fax: KeyValuePair,
                   @field:Json(name = "address1") val address1: KeyValuePair)

data class KeyValuePair(@field:Json(name = "\$t") val value: String)