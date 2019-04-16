package com.example.petmatcher.search

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.example.petmatcher.data.NetworkState
import com.example.petmatcher.data.api.organizations.Organization

/**
 * @author Lisa Watkins
 *
 * Paging boundary callback for Organizations list.
 */
class OrganizationBoundaryCallback(val networkRequest: (Boolean) -> Unit)
    : PagedList.BoundaryCallback<Organization>() {
    val networkState = MutableLiveData<NetworkState>()

    /**
     * Called when zero items are returned from the database
     */
    override fun onZeroItemsLoaded() {
        networkState.value = NetworkState.RUNNING
        networkRequest(false)
    }

    /**
     * Called when the last item in the database is loaded
     */
    override fun onItemAtEndLoaded(itemAtEnd: Organization) {
        networkRequest(false)
    }
}