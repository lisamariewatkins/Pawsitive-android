package com.example.petmatcher.search

import androidx.paging.PageKeyedDataSource
import com.example.network.organizations.Organization
import com.example.petmatcher.data.OrganizationRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class OrganizationDataSource constructor(private val organizationRepository: OrganizationRepository)
    : PageKeyedDataSource<String, Organization>() {

    // todo scope
    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, Organization>) {
        GlobalScope.launch {
            try {
                val response = organizationRepository.getSheltersAsync().await()
                val shelters = response.organizations
                val next = response.pagination.links.next.href

                callback.onResult(shelters, null, next)
            } catch (e: Exception) {
                // todo error handling
            }
        }
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Organization>) {
        GlobalScope.launch {
            try {
                val response = organizationRepository.getNextSheltersAsync(path = params.key).await()
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