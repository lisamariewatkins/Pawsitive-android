package com.example.petmatcher.search

import androidx.paging.PageKeyedDataSource
import com.example.network.organizations.Organization
import com.example.petmatcher.data.ShelterRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class ShelterDataSource constructor(private val shelterRepository: ShelterRepository)
    : PageKeyedDataSource<String, Organization>() {

    // todo scope
    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, Organization>) {
        GlobalScope.launch {
            try {
                val response = shelterRepository.getSheltersAsync().await()
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
                val response = shelterRepository.getNextSheltersAsync(path = params.key).await()
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