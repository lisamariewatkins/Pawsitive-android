package com.example.petmatcher.search

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.network.organizations.Organization
import com.example.network.organizations.OrganizationService
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class OrganizationRepository @Inject constructor(organizationService: OrganizationService, scope: CoroutineScope) {
    val organizationDataSourceFactory = OrganizationDataSourceFactory(organizationService, scope)

    fun getOrganizations(): LiveData<PagedList<Organization>> {
        val config = PagedList.Config.Builder()
            .setPageSize(20)
            .setEnablePlaceholders(false)
            .build()

        return LivePagedListBuilder<String, Organization>(organizationDataSourceFactory, config).build()
    }
}