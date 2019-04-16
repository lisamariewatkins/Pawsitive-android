package com.example.petmatcher.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.network.organizations.OrganizationService
import com.example.petmatcher.data.AppDatabase
import com.example.petmatcher.data.Result
import com.example.petmatcher.data.api.organizations.Organization
import com.example.petmatcher.data.api.organizations.OrganizationJsonResponse
import kotlinx.coroutines.*
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Repository class to retrieve a list of [Organization]s from the network
 */
class OrganizationRepository @Inject constructor(private val db: AppDatabase,
                                                 private val organizationService: OrganizationService,
                                                 val database: AppDatabase): CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    private var lastRequestedPage = 1

    fun getOrganizationsWithCaching(): LiveData<Result<Organization>> {
        val boundaryCallback = OrganizationBoundaryCallback(this::requestByPage)

        val config = PagedList.Config.Builder()
            .setPageSize(20)
            .setEnablePlaceholders(false)
            .build()

        val livePagedList = LivePagedListBuilder(database.organizationDao().getAllOrganizations(), config)
            .setBoundaryCallback(boundaryCallback)
            .build()

        val organizations =  Result(
            pagedList = livePagedList,
            networkState = boundaryCallback.networkState,
            refresh = {
                lastRequestedPage = 1
                requestByPage(refresh = true)
            }
        )

        val liveData = MutableLiveData<Result<Organization>>()
        liveData.value = organizations

        return liveData
    }

    // TODO: Can we use default parameters?
    private fun requestByPage(refresh: Boolean) {
        launch {
            try {
                // parse response
                val response = organizationService.getOrganizationsByPageAsync(lastRequestedPage++).await()
                // clear database if refresh
                if (refresh) db.organizationDao().deleteAllOrganizations()
                // update db with result
                insertResultIntoDb(response)
            } catch (e: Exception) {
                Log.d("Repo", "Error fetching organizations " + e.localizedMessage)
            }
        }
    }

    private fun insertResultIntoDb(body: OrganizationJsonResponse?) {
        body?.organizations?.let { organizations ->
            try {
                db.runInTransaction {
                    db.organizationDao().insert(organizations)
                }
            } catch (e: Exception) {
                Log.d("OrganizationRepository", e.localizedMessage)
            }
        }
    }

    /**
     * Exposes a way to cancel all children of [OrganizationDataSource.coroutineContext] to [OrganizationSearchViewModel]
     */
    internal fun cancelAllCoroutineChildren() {
        coroutineContext.cancelChildren()
    }
}