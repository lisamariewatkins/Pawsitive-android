package com.example.petmatcher.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.example.network.organizations.Organization
import com.example.network.organizations.OrganizationService
import com.example.petmatcher.data.NetworkState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

const val TAG = "OrganizationDataSource"

/**
 * Factory class for [OrganizationDataSource]. This holds a [MutableLiveData] instance containing the data source so that
 * we can observe the source (and all of its [MutableLiveData]) upstream.
 */
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
    val networkState = MutableLiveData<NetworkState>()

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, Organization>) {
        coroutineScope.launch {
            networkState.postValue(NetworkState.RUNNING)
            loadInitialAsync(callback)
        }
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Organization>) {
        coroutineScope.launch {
            loadNextAsync(url = params.key, callback = callback)
        }
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, Organization>) {
        // ignored, because we only append to our initial load
    }

    private suspend fun loadInitialAsync(callback: LoadInitialCallback<String, Organization>) {
        withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Fetching organizations on " + Thread.currentThread().name)

                // parse response
                val response = organizationService.getOrganizationsAsync().await()
                val shelters = response.organizations
                val next = response.pagination.links.next.href

                // update network state
                networkState.postValue(NetworkState.SUCCESS)

                callback.onResult(shelters, null, next)
            } catch (e: Exception) {
                Log.d(TAG, "Error fetching organizations " + e.localizedMessage)
                networkState.postValue(NetworkState.FAILURE)
            }
        }
    }

    private suspend fun loadNextAsync(url: String, callback: LoadCallback<String, Organization>) {
        withContext(Dispatchers.Default) {
            try {
                Log.d(TAG, "Fetching organizations on " + Thread.currentThread().name)

                // parse response
                val response = organizationService.getNextOrganizationsAsync(url).await()
                val shelters = response.organizations
                val next = response.pagination.links.next.href

                // update network state
                networkState.postValue(NetworkState.SUCCESS)

                callback.onResult(shelters, next)
            } catch (e: Exception) {
                Log.d(TAG, "Error fetching organizations " + e.localizedMessage)
                networkState.postValue(NetworkState.FAILURE)
            }
        }
    }
}