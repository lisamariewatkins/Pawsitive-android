package com.example.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM Favorites")
    fun getFavorites(): LiveData<List<Favorite>>

    @Query("SELECT * FROM Favorites WHERE petId=:id LIMIT 1")
    fun getFavorite(id: Int): LiveData<Favorite>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favorite: Favorite)
}