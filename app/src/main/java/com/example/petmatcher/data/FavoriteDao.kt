package com.example.petmatcher.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM $favoritesTableName")
    fun getFavorites(): LiveData<List<Favorite>>

    @Query("SELECT * FROM $favoritesTableName WHERE petId=:id LIMIT 1")
    fun getFavorite(id: String): LiveData<Favorite>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favorite: Favorite)
}