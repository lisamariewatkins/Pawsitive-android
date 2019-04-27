package com.example.petmatcher.search

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.network.organizations.OrganizationService
import com.example.database.OrganizationDao
import com.example.network.NetworkState
import com.example.network.Result
import com.example.network.organizations.Organization
import com.example.petmatcher.util.Logger
import kotlinx.coroutines.*
import java.lang.Exception
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

const val TAG = "OrganizationRepository"

/**
 * Repository class to retrieve a list of [Organization]s from the network
 */
class OrganizationRepository @Inject constructor(private val organizationService: OrganizationService,
                                                 val organizationDao: OrganizationDao,
                                                 val logger: Logger): CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    @VisibleForTesting
    internal val networkState = MutableLiveData<NetworkState>()
    // TODO: Account for number we have in database since we're using WorkManager
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

        scheduleOrganizationsSync()

        return MutableLiveData<Result<LiveData<PagedList<Organization>>>>().apply {
            value = organizations
        }
    }

    private fun scheduleOrganizationsSync() {
        val periodicRequest = PeriodicWorkRequestBuilder<OrganizationWorker>(1, TimeUnit.DAYS).build()
        WorkManager.getInstance().enqueueUniquePeriodicWork(
            organizationWorker,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicRequest)
    }

    internal suspend fun saveOrganizations(organizations: List<Organization>) {
        withContext(Dispatchers.IO) {
            try {
                organizationDao.insert(organizations)
            } catch (e: Exception) {
                logger.d(TAG, e.localizedMessage)
            }
        }
    }

    private fun requestByPage(refresh: Boolean) {
        launch {
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
    }

    private suspend fun clearOrganizations() {
        withContext(Dispatchers.IO) {
            organizationDao.deleteAllOrganizations()
        }
    }

    /**
     * Exposes a way to cancel all children of [coroutineContext] so we can link to a lifecycle somewhere
     */
    internal fun clear() {
        coroutineContext.cancelChildren()
    }
}

/**
 * @author Lisa Watkins
 *
 * Paging boundary callback for Organizations list.
 */
class OrganizationBoundaryCallback(val requestByPage: (Boolean) -> Unit)
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