package com.example.petmatcher.favorites

import com.example.network.petlist.Pet
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepository @Inject constructor() {
    private val favorites = ArrayList<Pet>()

    public fun addToFavorites(newPet: Pet) {
        favorites.add(newPet)
    }

    public fun getFavoritesList(): List<Pet> {
        return favorites
    }
}