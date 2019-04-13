package com.example.petmatcher.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.network.organizations.Organization
import com.example.network.organizations.OrganizationService
import com.example.petmatcher.data.OrganizationResult
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class OrganizationRepository @Inject constructor(organizationService: OrganizationService, scope: CoroutineScope) {
    private val organizationDataSourceFactory = OrganizationDataSourceFactory(organizationService, scope)

    fun getOrganizations(): LiveData<OrganizationResult<Organization>> {
        val config = PagedList.Config.Builder()
            .setPageSize(20)
            .setEnablePlaceholders(false)
            .build()

        val pagedList =  LivePagedListBuilder<String, Organization>(organizationDataSourceFactory, config).build()

        val organizations = OrganizationResult(
            pagedList = pagedList,
            networkState = Transformations.switchMap(organizationDataSourceFactory.organizationSource) {
                it.networkState
            },
            refresh = {
                organizationDataSourceFactory.organizationSource.value?.invalidate()
            }
        )

        val liveData = MutableLiveData<OrganizationResult<Organization>>()
        liveData.value = organizations

        return liveData
    }
}