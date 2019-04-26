package com.example.petmatcher.search

import androidx.paging.PagedList
import com.example.petmatcher.data.api.organizations.Organization
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * @author Lisa Watkins
 *
 * Paging boundary callback for Organizations list.
 */
class OrganizationBoundaryCallback(val requestByPage: suspend (Boolean) -> Unit,
                                   private val coroutineScope: CoroutineScope)
    : PagedList.BoundaryCallback<Organization>() {

    /**
     * Called when zero items are returned from the database
     */
    override fun onZeroItemsLoaded() {
        coroutineScope.launch {
            requestByPage(false)
        }
    }

    /**
     * Called when the last item in the database is loaded
     */
    override fun onItemAtEndLoaded(itemAtEnd: Organization) {
        coroutineScope.launch {
            requestByPage(false)
        }
    }
}