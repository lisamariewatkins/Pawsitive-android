package com.example.petmatcher.data

import com.example.network.animals.Animal
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepository @Inject constructor(private val favoriteDao: FavoriteDao) {

    suspend fun addToFavorites(newPet: Animal) {
        val favorite = Favorite(newPet.id,
            newPet.name,
            newPet.description,
            newPet.type,
            newPet.photos?.get(0)?.large!!
        )

        withContext(IO) {
            favoriteDao.insert(favorite)
        }
    }

    fun getFavoritesList() = favoriteDao.getFavorites()

    fun getFavorite(id: String) = favoriteDao.getFavorite(id)
}