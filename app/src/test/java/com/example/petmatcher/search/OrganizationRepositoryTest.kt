package com.example.petmatcher.search

import io.mockk.mockk

class OrganizationRepositoryTest {
    val mockDataSourceFactory: OrganizationDataSourceFactory = mockk()

    val organizationRepository = OrganizationRepository(mockDataSourceFactory)
}