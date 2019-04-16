package com.example.petmatcher.data

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.petmatcher.data.api.organizations.Organization

@Dao
interface OrganizationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(organizations: List<Organization>)

    @Query("SELECT * FROM organizations")
    fun getAllOrganizations(): DataSource.Factory<Int, Organization>

    @Query("DELETE FROM organizations")
    fun deleteAllOrganizations()

    @Query("SELECT COUNT(*) FROM organizations")
    fun getCount(): Int
}