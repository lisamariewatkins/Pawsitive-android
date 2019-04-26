package com.example.petmatcher.search

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.example.network.organizations.OrganizationService
import java.lang.Exception
import javax.inject.Inject

interface ChildWorkerFactory {
    fun create(appContext: Context, params: WorkerParameters): ListenableWorker
}

class OrganizationWorker @Inject constructor(
    context: Context,
    workerParameters: WorkerParameters,
    private val organizationService: OrganizationService,
    private val organizationRepository: OrganizationRepository)
    : CoroutineWorker(context, workerParameters) {


    override suspend fun doWork(): Result {
        return try {
            val result = organizationService.getOrganizationsAsync().await()
            organizationRepository.insertResultIntoDb(result.organizations)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    class Factory @Inject constructor(
        private val organizationServiceProvider: OrganizationService,
        private val organizationRepository: OrganizationRepository
    ) : ChildWorkerFactory {
        override fun create(appContext: Context, params: WorkerParameters): CoroutineWorker {
            return OrganizationWorker(
                appContext,
                params,
                organizationServiceProvider,
                organizationRepository
            )
        }
    }
}