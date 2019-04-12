package com.example.petmatcher.data

import com.example.network.organizations.OrganizationJsonResponse
import com.example.network.organizations.OrganizationService
import kotlinx.coroutines.Deferred
import javax.inject.Inject

class OrganizationRepository @Inject constructor(private val organizationService: OrganizationService) {

    fun getSheltersAsync(): Deferred<OrganizationJsonResponse> {
        return organizationService.getOrganizationsAsync()
    }

    fun getNextSheltersAsync(path: String): Deferred<OrganizationJsonResponse> {
        return organizationService.getNextOrganizationsAsync(path)
    }
}