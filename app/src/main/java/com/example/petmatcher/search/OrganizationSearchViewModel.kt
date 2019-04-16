package com.example.petmatcher.search

import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.example.petmatcher.data.NetworkState
import com.example.petmatcher.data.api.organizations.Organization
import javax.inject.Inject

/**
 * @author Lisa Watkins
 *
 * [ViewModel] behind [OrganizationSearchFragment]. We call [Transformations.switchMap] on the repository result
 * to keep various data points up to date with the source. All events transmitted by the repository result will be
 * retransmitted by the switch-mapped [LiveData].
 *
 */
class OrganizationSearchViewModel @Inject constructor(private val organizationRepository: OrganizationRepository)
    : ViewModel() {

    private val repoResult = organizationRepository.getOrganizationsWithCaching()

    val organizationsList: LiveData<PagedList<Organization>> = Transformations.switchMap(repoResult) {
        it.pagedList
    }

    val networkState: LiveData<NetworkState> = Transformations.switchMap(repoResult) {
        it.networkState
    }

    /**
     * Refresh the data from the network whenever we visit this fragment for the first time. Since the fragment is added
     * to the backstack when the user navigates away, this should only be called the first time the user visits this screen on app launch.
     */
    init {
        refresh()
    }

    override fun onCleared() {
        organizationRepository.cancelAllCoroutineChildren()
        super.onCleared()
    }

    /**
     * Refresh organizations list by invalidating the data source.
     */
    fun refresh() {
        repoResult.value?.refresh?.invoke()
    }

    fun displayViewByNetworkState(contentLayout: View, progressBar: ProgressBar, state: NetworkState) {
        when (state) {
            NetworkState.RUNNING -> {
                contentLayout.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            }
            NetworkState.SUCCESS -> {
                contentLayout.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
            NetworkState.FAILURE -> {
                // TODO: handle various error states
            }
        }
    }
}