package com.example.petmatcher.data

import com.example.network.animals.*
import com.example.network.animals.Contact

class MockAnimalJsonResponse {
    fun with(numberOfAnimals: Int): AnimalJsonResponse {
        val animalList = ArrayList<Animal>()

        for (i in 1..numberOfAnimals) {
            animalList.add(Animal(
                123,
                "orgId",
                "url",
                "type",
                "species",
                Breeds("primary",
                    "secondary",
                    "tertiary"),
                Colors("primary",
                    "secondary",
                    "tertiary"),
                "age",
                "gender",
                "size",
                "coat",
                Attributes(spayedNeutered = false,
                    house_trained = false,
                    declawed = false,
                    specialNeeds = false,
                    shots_current = false),
                Environment(children = false,
                    dogs = false,
                    cats = false),
                listOf("tags"),
                "name",
                "description",
                listOf(
                    Photo("small",
                    "medium",
                    "large",
                    "full")
                ),
                "status",
                "publishedAt",
                Contact("email",
                    "phone",
                    Address("address1",
                        "address2",
                        "city",
                        "state",
                        "postcode",
                        "country")),
                Pagination(0,
                    0,
                    0,
                    0,
                    Links(Link("previous"),
                        Link("next"))
                )
            ))
        }

        return AnimalJsonResponse(animalList)
    }
}