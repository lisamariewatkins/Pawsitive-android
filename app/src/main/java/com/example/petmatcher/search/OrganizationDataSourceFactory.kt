package com.example.petmatcher.search

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.network.organizations.Organization
import com.example.petmatcher.data.ShelterRepository

class OrganizationDataSourceFactory(private val shelterRepository: ShelterRepository): DataSource.Factory<String, Organization>() {
    val organizationSource = MutableLiveData<ShelterDataSource>()
    override fun create(): DataSource<String, Organization> {
        val source = ShelterDataSource(shelterRepository)
        organizationSource.postValue(source)
        return source
    }
}