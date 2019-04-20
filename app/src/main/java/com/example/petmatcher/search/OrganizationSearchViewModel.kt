package com.example.petmatcher.search

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.example.petmatcher.R
import com.example.petmatcher.networkutil.NetworkState
import com.example.petmatcher.data.api.organizations.Organization
import kotlinx.coroutines.launch
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

    fun refresh() {
        viewModelScope.launch {
            organizationRepository.refresh()
        }
    }

    fun retry() {
        // TODO: Implement
    }

    fun displayViewByNetworkState(contentLayout: View, loadingLayout: FrameLayout, state: NetworkState) {
        when (state) {
            NetworkState.RUNNING -> {
                contentLayout.visibility = View.GONE
                loadingLayout.visibility = View.VISIBLE
                loadingLayout.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.VISIBLE
                loadingLayout.findViewById<LinearLayout>(R.id.network_error).visibility = View.GONE
            }
            NetworkState.SUCCESS -> {
                loadingLayout.visibility = View.GONE
                contentLayout.visibility = View.VISIBLE
            }
            NetworkState.FAILURE -> {
                contentLayout.visibility = View.VISIBLE
                loadingLayout.visibility = View.GONE
                // TODO: Discern between empty and non empty cache
            }
        }
    }

    override fun onCleared() {
        organizationRepository.clear()
        super.onCleared()
    }
}