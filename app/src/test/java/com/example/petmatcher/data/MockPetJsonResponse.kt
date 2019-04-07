package com.example.petmatcher.data

import com.example.network.petlist.*

class MockPetJsonResponse {
    fun with(numberOfPets: Int): PetJsonResponse {
        val petList = ArrayList<Pet>()
        val petObject = PetObject(petList)
        val petFinder = PetFinder(KeyValuePair("25"), petObject)
        val photoInfo = listOf(PhotoInfo("size", "url", "id"),
            PhotoInfo("size", "url", "id"),
            PhotoInfo("size", "url", "id"),
            PhotoInfo("size", "url", "id"))

        for (i in 1..numberOfPets) {
            petList.add(Pet(
                KeyValuePair("status"),
                Contact(
                    KeyValuePair("phone"),
                    KeyValuePair(""),
                    KeyValuePair(""),
                    KeyValuePair(""),
                    KeyValuePair(""),
                    KeyValuePair(""),
                    KeyValuePair(""),
                    KeyValuePair("")
                ),
                KeyValuePair("age"),
                KeyValuePair("size"),
                Photos(PhotoList(photoInfo)),
                KeyValuePair("id"),
                KeyValuePair("name"),
                KeyValuePair("sex"),
                KeyValuePair("description"),
                KeyValuePair("mix"),
                KeyValuePair("shleterId"),
                KeyValuePair("lastUpdate"),
                KeyValuePair("animal")
            ))
        }

        return PetJsonResponse("version", petFinder)
    }
}