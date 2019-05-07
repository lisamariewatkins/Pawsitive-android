package com.example.database

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.models.organization.Organization

/**
 * @author Lisa Watkins
 *
 * Database access object for Organizations table.
 *
 * [getAllOrganizations] returns a [DataSource.Factory] that creates a [DataSource]
 * containing the information from the local Room database. When this [DataSource.Factory] is passed to a [PagedListBuilder],
 * a [LiveData<PagedList>] is created containing a snapshot of the data in the [DataSource].
 *
 * This makes the app more efficient because rather than loading all the entries in the database, we load chunks at a time.
 */
@Dao
interface OrganizationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(organizations: List<com.example.models.organization.Organization>)

    @Query("SELECT * FROM organizations")
    fun getAllOrganizations(): DataSource.Factory<Int, com.example.models.organization.Organization>

    @Query("DELETE FROM organizations")
    fun deleteAllOrganizations()

    @Query("SELECT COUNT(*) FROM organizations")
    fun getCount(): Int
}