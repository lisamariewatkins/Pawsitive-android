package com.example.petmatcher.favorites

import com.example.network.petlist.Pet
import javax.inject.Inject

class FavoritesRepository @Inject constructor() {
    private val favorites = ArrayList<Pet>()

    public fun addToFavorites(newPet: Pet) {
        favorites.add(newPet)
    }
}