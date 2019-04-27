package com.example.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.network.organizations.Organization

/**
 * @author Lisa Watkins
 *
 * Serves as the main access point for underlying persisted relational database. Room provides an abstraction over SQLite to
 * allow easy database access.
 *
 * Since creating a [RoomDatabase] instance is fairly expensive, you should use a singleton design pattern to ensure only
 * one instance is created for the app's process.
 */
// TODO: Singleton pattern
@Database(entities = [Favorite::class, Organization::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun organizationDao(): OrganizationDao
}