package com.example.petmatcher.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.example.network.organizations.Organization
import com.example.network.organizations.OrganizationService
import javax.inject.Inject

/**
 * @author Lisa Watkins
 *
 * [ViewModel] behind [OrganizationSearchFragment]
 */
class OrganizationSearchViewModel @Inject constructor(organizationService: OrganizationService)
    : ViewModel() {
    private val organizationRepository = OrganizationRepository(organizationService, viewModelScope)

    val organizationsList: LiveData<PagedList<Organization>> = organizationRepository.getOrganizations()

    /**
     * Refresh organizations list by invalidating the data source.
     */
    fun refresh() {
        organizationRepository.organizationDataSourceFactory.organizationSource.value?.invalidate()
    }
}