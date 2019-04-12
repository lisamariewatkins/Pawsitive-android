package com.example.petmatcher.search

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.network.organizations.Organization
import com.example.petmatcher.data.OrganizationRepository

class OrganizationDataSourceFactory(private val organizationRepository: OrganizationRepository): DataSource.Factory<String, Organization>() {
    val organizationSource = MutableLiveData<OrganizationDataSource>()
    override fun create(): DataSource<String, Organization> {
        val source = OrganizationDataSource(organizationRepository)
        organizationSource.postValue(source)
        return source
    }
}