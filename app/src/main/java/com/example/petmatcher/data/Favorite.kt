package com.example.petmatcher.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Favorites")
data class Favorite(
    @PrimaryKey val petId: Int,
    val name: String,
    val description: String,
    val breed: String,
    val imageUrl: String = ""
) {
    override fun toString() = name
}