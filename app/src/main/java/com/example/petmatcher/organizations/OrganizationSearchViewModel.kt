package com.example.petmatcher.organizations

import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.example.petmatcher.R
import com.example.network.util.NetworkState
import com.example.models.organization.Organization
import javax.inject.Inject

/**
 * @author Lisa Watkins
 *
 * [ViewModel] behind [OrganizationSearchFragment]. We call [Transformations.switchMap] on the repository result
 * to keep the paged list up to date with the source. All events transmitted by the repository result will be
 * retransmitted by the switch-mapped [LiveData], while removing the previous [LiveData] source.
 *
 */
class OrganizationSearchViewModel @Inject constructor(
    private val organizationRepository: OrganizationRepository)
    : ViewModel() {

    private val repoResult = organizationRepository.getOrganizations()

    val organizationsList: LiveData<PagedList<Organization>> = Transformations.switchMap(repoResult){
        it.pagedList
    }

    val networkState: LiveData<NetworkState> = organizationRepository.networkState

    fun refresh() {
        repoResult.value?.refresh?.invoke()
    }

    fun displayViewByNetworkState(contentLayout: View, loadingLayout: FrameLayout, state: NetworkState) {
        when (state) {
            NetworkState.RUNNING -> {
                showProgressSpinner(contentLayout, loadingLayout)
            }
            NetworkState.SUCCESS -> {
                showContent(contentLayout, loadingLayout)
            }
            NetworkState.FAILURE -> {
                organizationsList.value?.let {
                    if (it.isEmpty()) {
                        showError(contentLayout, loadingLayout)
                    } else {
                        showContent(contentLayout, loadingLayout)
                    }
                } ?: run {
                    showError(contentLayout, loadingLayout)
                }
            }
        }
    }

    private fun showProgressSpinner(contentLayout: View, loadingLayout: FrameLayout) {
        contentLayout.visibility = View.GONE
        loadingLayout.visibility = View.VISIBLE
        loadingLayout.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.VISIBLE
        loadingLayout.findViewById<LinearLayout>(R.id.network_error).visibility = View.GONE
    }

    private fun showContent(contentLayout: View, loadingLayout: FrameLayout) {
        loadingLayout.visibility = View.GONE
        contentLayout.visibility = View.VISIBLE
    }

    private fun showError(contentLayout: View, loadingLayout: FrameLayout) {
        contentLayout.visibility = View.GONE
        loadingLayout.visibility = View.VISIBLE
        loadingLayout.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.GONE
        loadingLayout.findViewById<LinearLayout>(R.id.network_error).visibility = View.VISIBLE
    }

    override fun onCleared() {
        organizationRepository.onCleared()
        super.onCleared()
    }
}