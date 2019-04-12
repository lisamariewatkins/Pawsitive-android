package com.example.petmatcher.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.network.organizations.Organization
import com.example.petmatcher.data.OrganizationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class OrganizationSearchViewModel @Inject constructor(private val organizationRepository: OrganizationRepository)
    : ViewModel() {
    private val organizationSourceFactory = OrganizationDataSourceFactory(organizationRepository)
    var organizationsList: LiveData<PagedList<Organization>>

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(25)
            .setEnablePlaceholders(false)
            .build()

        organizationsList  = LivePagedListBuilder<String, Organization>(organizationSourceFactory, config).build()
    }

    fun refresh() {
        organizationSourceFactory.organizationSource.value?.invalidate()
    }

    @ExperimentalCoroutinesApi
    override fun onCleared() {
        super.onCleared()
    }
}