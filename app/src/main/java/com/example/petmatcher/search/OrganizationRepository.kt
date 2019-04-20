package com.example.petmatcher.search

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.network.organizations.OrganizationService
import com.example.petmatcher.data.OrganizationDao
import com.example.petmatcher.networkutil.NetworkState
import com.example.petmatcher.networkutil.Result
import com.example.petmatcher.data.api.organizations.Organization
import com.example.petmatcher.data.api.organizations.OrganizationJsonResponse
import com.example.petmatcher.util.Logger
import kotlinx.coroutines.*
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

const val TAG = "OrganizationRepository"

/**
 * Repository class to retrieve a list of [Organization]s from the network
 */
// TODO: Why is the PageList jumping?
class OrganizationRepository @Inject constructor(private val organizationService: OrganizationService,
                                                 val organizationDao: OrganizationDao,
                                                 val logger: Logger): CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    @VisibleForTesting
    internal val networkState = MutableLiveData<NetworkState>()
    private var lastRequestedPage = 1

    fun getOrganizationsWithCaching(): LiveData<Result<LiveData<PagedList<Organization>>>> {
        val boundaryCallback = OrganizationBoundaryCallback(this::requestByPage, this)

        val config = PagedList.Config.Builder()
            .setPageSize(20)
            .setEnablePlaceholders(false)
            .build()

        val livePagedList = LivePagedListBuilder(organizationDao.getAllOrganizations(), config)
            .setBoundaryCallback(boundaryCallback)
            .build()

        val organizations = Result(
            pagedList = livePagedList,
            networkState = networkState,
            refresh = {
                // refresh()
            },
            retry = {
                // TODO: Implement this
            })

        return MutableLiveData<Result<LiveData<PagedList<Organization>>>>().apply {
            value = organizations
        }
    }

    internal suspend fun refresh() {
        networkState.postValue(NetworkState.RUNNING)
        lastRequestedPage = 1
        logger.d(TAG, "Requesting to refresh organizations from " + Thread.currentThread().name)
        try {
            val response = organizationService.getOrganizationsByPageAsync(lastRequestedPage++).await()
            refreshDatabase(response)
            networkState.postValue(NetworkState.SUCCESS)
        } catch (e: Exception) {
            logger.d(TAG, "Error fetching organizations " + e.localizedMessage)
            networkState.postValue(NetworkState.FAILURE)
        }
    }

    internal suspend fun requestByPage() {
        logger.d(TAG, "Requesting organizations from " + Thread.currentThread().name)
        try {
            val response = organizationService.getOrganizationsByPageAsync(lastRequestedPage++).await()
            insertResultIntoDb(response)
        } catch (e: Exception) {
            logger.d(TAG, "Error fetching organizations " + e.localizedMessage)
        }
    }

    private suspend fun refreshDatabase(body: OrganizationJsonResponse?) {
        body?.organizations?.let {
            withContext(Dispatchers.IO) {
                logger.d(TAG, "Refreshing organizations in database from " + Thread.currentThread().name)
                organizationDao.deleteAllOrganizations()
                insertResultIntoDb(body)
            }
        }
    }

    private suspend fun insertResultIntoDb(body: OrganizationJsonResponse?) {
        body?.organizations?.let { organizations ->
            withContext(Dispatchers.IO) {
                logger.d(TAG, "Inserting organizations in database from " + Thread.currentThread().name)
                try {
                    organizationDao.insert(organizations)
                } catch (e: Exception) {
                    logger.d(TAG, e.localizedMessage)
                }
            }
        }
    }

    /**
     * Exposes a way to cancel all children of [coroutineContext] so we can link to a lifecycle somewhere
     */
    internal fun clear() {
        coroutineContext.cancelChildren()
    }
}