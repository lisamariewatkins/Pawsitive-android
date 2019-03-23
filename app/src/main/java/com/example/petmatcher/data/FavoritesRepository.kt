package com.example.petmatcher.data

import com.example.network.petlist.Pet
import com.example.petmatcher.data.Favorite
import com.example.petmatcher.data.FavoriteDao
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepository @Inject constructor(private val favoriteDao: FavoriteDao) {

    suspend fun addToFavorites(newPet: Pet) {
        val favorite = Favorite(newPet.id.value,
            newPet.name.value,
            newPet.description.value,
            newPet.animal.value,
            newPet.media.photos?.photoList?.get(3)?.url!! // todo fix this
        )

        withContext(IO) {
            favoriteDao.insert(favorite)
        }
    }

    fun getFavoritesList() = favoriteDao.getFavorites()

    fun getFavorite(id: String) = favoriteDao.getFavorite(id)
}