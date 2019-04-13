package com.example.petmatcher.search

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.example.network.organizations.Organization
import com.example.network.organizations.OrganizationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.Exception

/**
 * Factory class for [OrganizationDataSource].
 */
// TODO: Decide if we are going to wrap the shelter list in a data class, if not, remove LiveData
class OrganizationDataSourceFactory(private val organizationService: OrganizationService,
                                    private val scope: CoroutineScope): DataSource.Factory<String, Organization>() {
    val organizationSource = MutableLiveData<OrganizationDataSource>()

    override fun create(): DataSource<String, Organization> {
        val source = OrganizationDataSource(organizationService, scope)
        organizationSource.postValue(source)
        return source
    }
}

/**
 * [PageKeyedDataSource] for organizations list. The pagination mechanism for the PetFinder API passes a "next" URL for the
 * next page of data. Pages are returned with record sizes of 20.
 */
class OrganizationDataSource(private val organizationService: OrganizationService, private val coroutineScope: CoroutineScope)
    : PageKeyedDataSource<String, Organization>() {

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, Organization>) {
        coroutineScope.launch {
            try {
                val response = organizationService.getOrganizationsAsync().await()
                val shelters = response.organizations
                val next = response.pagination.links.next.href

                callback.onResult(shelters, null, next)
            } catch (e: Exception) {
                // todo error handling
            }
        }
    }

    // todo scope
    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Organization>) {
        coroutineScope.launch {
            try {
                val response = organizationService.getNextOrganizationsAsync(url = params.key).await()
                val shelters = response.organizations
                val next = response.pagination.links.next.href

                callback.onResult(shelters, next)
            } catch (e: Exception) {
                // todo error handling
            }
        }
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, Organization>) {
        // ignored, because we only append to our initial load
    }
}