package com.example.petmatcher.data

import androidx.room.Entity
import androidx.room.PrimaryKey

const val favoritesTableName = "Favorites"

@Entity(tableName = favoritesTableName)
data class Favorite(
    @PrimaryKey val petId: String,
    val name: String,
    val description: String,
    val breed: String,
    val imageUrl: String = ""
) {
    override fun toString() = name
}