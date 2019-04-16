package com.example.petmatcher.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.petmatcher.data.api.organizations.Organization

@Database(entities = [Favorite::class, Organization::class], version = 5)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun organizationDao(): OrganizationDao
}