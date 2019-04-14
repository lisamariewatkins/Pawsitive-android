package com.example.petmatcher.search

import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.example.network.organizations.Organization
import com.example.network.organizations.OrganizationService
import com.example.petmatcher.data.NetworkState
import javax.inject.Inject

/**
 * @author Lisa Watkins
 *
 * [ViewModel] behind [OrganizationSearchFragment]. We call [Transformations.switchMap] on the repository result
 * to keep various data points up to date with the source. All events transmitted by the repository result will be
 * retransmitted by the switch-mapped [LiveData].
 *
 */
class OrganizationSearchViewModel @Inject constructor(val organizationRepository: OrganizationRepository)
    : ViewModel() {

    private val repoResult = organizationRepository.getOrganizations()

    val organizationsList: LiveData<PagedList<Organization>> = Transformations.switchMap(repoResult) {
        it.pagedList
    }

    val networkState: LiveData<NetworkState> = Transformations.switchMap(repoResult) {
        it.networkState
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
        contentLayout.visibility = if (state == NetworkState.SUCCESS) View.VISIBLE else View.GONE
        progressBar.visibility = if (state == NetworkState.SUCCESS || state == NetworkState.FAILURE) View.GONE else View.VISIBLE

        if (state == NetworkState.FAILURE) {
            // Toast.makeText(context, getString(R.string.error_message), Toast.LENGTH_LONG).show()
        }
    }
}