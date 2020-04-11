package com.example.petmatcher.organizations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.network.OrganizationService
import com.example.database.OrganizationDao
import com.example.network.util.NetworkState
import com.example.network.util.Result
import com.example.models.organization.Organization
import com.example.petmatcher.util.Logger
import com.example.petmatcher.util.WithDefaultCoroutineScope
import kotlinx.coroutines.*
import java.lang.Exception
import javax.inject.Inject

const val TAG = "OrganizationRepository"

/**
 * Repository class to retrieve a list of [Organization]s from the network
 */
class OrganizationRepository @Inject constructor(private val organizationService: OrganizationService,
                                                     private val organizationDao: OrganizationDao,
                                                     private val logger: Logger)
    : WithDefaultCoroutineScope {

    val networkState = MutableLiveData<NetworkState>()
    private var lastRequestedPage = 1

    fun getOrganizations(): LiveData<Result<LiveData<PagedList<Organization>>>> {
        val boundaryCallback = OrganizationBoundaryCallback(this::requestByPage)

        val config = PagedList.Config.Builder()
            .setPageSize(20)
            .setEnablePlaceholders(false)
            .build()

        val livePagedList = LivePagedListBuilder(organizationDao.getAllOrganizations(), config)
            .setBoundaryCallback(boundaryCallback)
            .build()

        val organizations = Result(
            pagedList = livePagedList,
            refresh = {
                requestByPage(refresh = true)
            },
            retry = {
                // TODO: Implement this
            })

        return MutableLiveData<Result<LiveData<PagedList<Organization>>>>().apply {
            value = organizations
        }
    }

    private suspend fun saveOrganizations(organizations: List<Organization>) = withContext(Dispatchers.IO) {
        try {
            organizationDao.insert(organizations)
        } catch (e: Exception) {
            logger.d(TAG, e.localizedMessage)
        }
    }

    private fun requestByPage(refresh: Boolean) = launch {
        if (refresh) {
            networkState.postValue(NetworkState.RUNNING)
            lastRequestedPage = 1
        }

        try {
            val response = organizationService.getOrganizationsByPageAsync(lastRequestedPage++).await()
            if (refresh) clearOrganizations()
            saveOrganizations(response.organizations)
            networkState.postValue(NetworkState.SUCCESS)
        } catch (e: Exception) {
            logger.d(TAG, "Error fetching organizations " + e.localizedMessage)
            networkState.postValue(NetworkState.FAILURE)
        }
    }

    private suspend fun clearOrganizations() = withContext(Dispatchers.IO){
        organizationDao.deleteAllOrganizations()
    }

    fun onCleared() {
        cancelAllChildren()
    }
}

/**
 * @author Lisa Watkins
 *
 * Paging boundary callback for Organizations list.
 */
class OrganizationBoundaryCallback(val requestByPage: Function1<Boolean, Job>)
    : PagedList.BoundaryCallback<Organization>() {

    /**
     * Called when zero items are returned from the database
     */
    override fun onZeroItemsLoaded() {
        requestByPage(false)
    }

    /**
     * Called when the last item in the database is loaded
     */
    override fun onItemAtEndLoaded(itemAtEnd: Organization) {
        requestByPage(false)
    }
}