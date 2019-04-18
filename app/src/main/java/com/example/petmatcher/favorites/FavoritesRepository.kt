package com.example.petmatcher.favorites

import android.util.Log
import com.example.network.animals.Animal
import com.example.petmatcher.data.Favorite
import com.example.petmatcher.data.FavoriteDao
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepository @Inject constructor(private val favoriteDao: FavoriteDao) {

    suspend fun addToFavorites(newPet: Animal) {
        val favorite = Favorite(
            newPet.id,
            newPet.name,
            newPet.description,
            newPet.type,
            newPet.photos?.get(0)?.large!!
        )

        withContext(IO) {
            try {
                favoriteDao.insert(favorite)
            } catch (e: Exception){
                Log.e("FavoritesRepository", e.localizedMessage)
            }
        }
    }

    fun getFavoritesList() = favoriteDao.getFavorites()

    fun getFavorite(id: Int) = favoriteDao.getFavorite(id)
}