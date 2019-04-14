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
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

const val TAG = "OrganizationDataSource"

/**
 * Factory class for [OrganizationDataSource]. This holds a [MutableLiveData] instance containing the data source so that
 * we can observe the source (and all of its [MutableLiveData]) upstream.
 *
 * We MUST new up an instance of [OrganizationDataSource] rather than inject via Dagger because if we call [PageKeyedDataSource.invalidate] we
 * will maintain an invalid instance of [OrganizationDataSource] rather than create a new instance.
 */
class OrganizationDataSourceFactory @Inject constructor(val organizationService: OrganizationService)
    : DataSource.Factory<String, Organization>() {
    val organizationSource = MutableLiveData<OrganizationDataSource>()

    override fun create(): DataSource<String, Organization> {
        val source = OrganizationDataSource(organizationService)
        organizationSource.postValue(source)
        return source
    }
}

/**
 * [PageKeyedDataSource] for organizations list. The pagination mechanism for the PetFinder API passes a "next" URL for the
 * next page of data. Pages are returned with record sizes of 20.
 */
// TODO: Proper error handling with coroutines
// TODO: Turn on logging
class OrganizationDataSource(private val organizationService: OrganizationService)
    : PageKeyedDataSource<String, Organization>(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default
    val networkState = MutableLiveData<NetworkState>()

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, Organization>) {
        launch {
            networkState.postValue(NetworkState.RUNNING)
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

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Organization>) {
        launch {
            try {
                Log.d(TAG, "Fetching organizations on " + Thread.currentThread().name)

                // parse response
                val response = organizationService.getNextOrganizationsAsync(url = params.key).await()
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

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, Organization>) {
        // ignored, because we only append to our initial load
    }
}