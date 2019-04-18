package com.example.petmatcher.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.network.organizations.OrganizationService
import com.example.petmatcher.data.AppDatabase
import com.example.petmatcher.networkutil.NetworkState
import com.example.petmatcher.networkutil.Result
import com.example.petmatcher.data.api.organizations.Organization
import com.example.petmatcher.data.api.organizations.OrganizationJsonResponse
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
                                                 val database: AppDatabase): CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    private val networkState = MutableLiveData<NetworkState>()
    private var lastRequestedPage = 1

    fun getOrganizationsWithCaching(): LiveData<Result<LiveData<PagedList<Organization>>>> {
        val boundaryCallback = OrganizationBoundaryCallback(this::requestByPage)

        val config = PagedList.Config.Builder()
            .setPageSize(20)
            .setEnablePlaceholders(false)
            .build()

        val livePagedList = LivePagedListBuilder(database.organizationDao().getAllOrganizations(), config)
            .setBoundaryCallback(boundaryCallback)
            .build()

        val organizations = Result(
            pagedList = livePagedList,
            networkState = networkState,
            refresh = {
                refresh()
            },
            retry = {
                refresh()
            })

        return MutableLiveData<Result<LiveData<PagedList<Organization>>>>().apply {
            value = organizations
        }
    }

    private fun refresh() {
        networkState.postValue(NetworkState.RUNNING)
        lastRequestedPage = 1
        launch {
            Log.d(TAG, "Requesting to refresh organizations from " + Thread.currentThread().name)
            try {
                val response = organizationService.getOrganizationsByPageAsync(lastRequestedPage++).await()
                refreshDatabase(response)
                networkState.postValue(NetworkState.SUCCESS)
            } catch (e: Exception) {
                Log.d(TAG, "Error fetching organizations " + e.localizedMessage)
                networkState.postValue(NetworkState.FAILURE)
            }
        }
    }

    private fun requestByPage() {
        launch {
            Log.d(TAG, "Requesting organizations from " + Thread.currentThread().name)
            try {
                val response = organizationService.getOrganizationsByPageAsync(lastRequestedPage++).await()
                insertResultIntoDb(response)
            } catch (e: Exception) {
                Log.d(TAG, "Error fetching organizations " + e.localizedMessage)
            }
        }
    }

    private suspend fun refreshDatabase(body: OrganizationJsonResponse?) {
        body?.organizations?.let {
            withContext(Dispatchers.IO) {
                Log.d(TAG, "Refreshing organizations in database from " + Thread.currentThread().name)
                database.runInTransaction {
                    database.organizationDao().deleteAllOrganizations()
                }
                insertResultIntoDb(body)
            }
        }
    }

    private suspend fun insertResultIntoDb(body: OrganizationJsonResponse?) {
        body?.organizations?.let { organizations ->
            withContext(Dispatchers.IO) {
                Log.d(TAG, "Inserting organizations in database from " + Thread.currentThread().name)
                try {
                    database.runInTransaction {
                        database.organizationDao().insert(organizations)
                    }
                } catch (e: Exception) {
                    Log.d(TAG, e.localizedMessage)
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