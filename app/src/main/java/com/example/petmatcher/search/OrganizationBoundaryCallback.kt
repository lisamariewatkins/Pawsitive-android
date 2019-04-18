package com.example.petmatcher.search

import androidx.paging.PagedList
import com.example.petmatcher.data.api.organizations.Organization

/**
 * @author Lisa Watkins
 *
 * Paging boundary callback for Organizations list.
 */
class OrganizationBoundaryCallback(val requestByPage: () -> Unit)
    : PagedList.BoundaryCallback<Organization>() {

    /**
     * Called when zero items are returned from the database
     */
    override fun onZeroItemsLoaded() {
        requestByPage()
    }

    /**
     * Called when the last item in the database is loaded
     */
    override fun onItemAtEndLoaded(itemAtEnd: Organization) {
        requestByPage()
    }
}