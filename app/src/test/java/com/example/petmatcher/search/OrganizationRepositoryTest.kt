package com.example.petmatcher.search

import androidx.paging.PageKeyedDataSource
import com.example.network.organizations.OrganizationService
import com.example.database.OrganizationDao
import com.example.network.organizations.Organization
import com.example.petmatcher.data.generateListOfMockOrganizations
import com.example.petmatcher.data.generateMockOrganizationJsonResponse
import com.example.network.NetworkState
import com.example.petmatcher.testextensions.InstantExecutorExtension
import com.example.petmatcher.util.Logger
import io.mockk.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.IOException

@ExtendWith(InstantExecutorExtension::class)
class OrganizationRepositoryTest {

    private val mockOrganizationService: OrganizationService = mockk()
    private val mockOrganizationDao: OrganizationDao = mockk(relaxUnitFun = true)
    private val mockLogger: Logger = mockk(relaxUnitFun = true)

    private val organizationRepository = OrganizationRepository(
        organizationService = mockOrganizationService,
        organizationDao = mockOrganizationDao,
        logger = mockLogger)

    @BeforeEach
    fun setUpMock() {
        clearAllMocks()
    }

    /**
     * Verify that if we have data in the database, we load that data into a [PagedList]
     */
    @Test
    @ExtendWith(InstantExecutorExtension::class)
    fun `verify organizations returned from database`() {
        // Arrange
        every {
            mockOrganizationDao.getAllOrganizations()
        } returns MockDataSourceFactory()

        // Exercise
        organizationRepository.getOrganizations().observeForever { result ->
            result.pagedList.observeForever {pagedListOfOrganizations ->
                // Assert
                Assert.assertEquals(pagedListOfOrganizations.size, 10)
            }
        }
    }

    /**
     * Test methods for the refresh functionality
     */
    @Nested
    inner class Refresh {
        /**
         * Verify that if we return a response from the network, we delete any existing entries in the database and insert
         * the new list from the response.
         */
        @Test
        @ExperimentalCoroutinesApi
        fun `verify refresh returns list from network`() = runBlocking {
            // Arrange
            val mockJsonResponse = CompletableDeferred(generateMockOrganizationJsonResponse())
            every { mockOrganizationService.getOrganizationsByPageAsync(any()) } returns mockJsonResponse

            // Exercise
            organizationRepository.refresh()

            // Assert
            val expected = mockJsonResponse.getCompleted()
            verifySequence {
                mockOrganizationDao.deleteAllOrganizations()
                mockOrganizationDao.insert(expected.organizations)
            }
        }

        /**
         * Verify that if we have a failed network request, we don't clear the database or call any insert methods. This ensures
         * that the cached data is still available to be displayed. We also want to update [NetworkState] with the failure.
         */
        @Test
        fun `verify failed refresh updates network state`() = runBlocking {
            // Arrange
            every {
                mockOrganizationService.getOrganizationsByPageAsync(any())
            } throws IOException("Something bad happened.")

            // Exercise
            organizationRepository.refresh()

            // Assert
            verify(exactly = 0) { mockOrganizationDao.deleteAllOrganizations() }
            verify(exactly = 0) {mockOrganizationDao.insert(any())}

            organizationRepository.networkState.observeForever {
                Assert.assertEquals(it, NetworkState.FAILURE)
            }
        }
    }


    /**
     * Verify that a successful network request results in a database insertion.
     */
    @Test
    fun `verify request by page returns result from network`() = runBlocking {
        // Arrange
        val mockJsonResponse = CompletableDeferred(generateMockOrganizationJsonResponse())
        every {
            mockOrganizationService.getOrganizationsByPageAsync(any())
        } returns mockJsonResponse

        // Exercise
        organizationRepository.requestByPage()

        // Assert
        verify { mockOrganizationDao.insert(any()) }
    }

    /**
     * Mock factory to be returned by mock DAO
     */
    class MockDataSourceFactory: androidx.paging.DataSource.Factory<Int, Organization>() {
        override fun create(): androidx.paging.DataSource<Int, Organization> {
            return MockDataSource()
        }
    }

    /**
     * Mock data source that returns a list of 10 organizations
     */
    class MockDataSource: PageKeyedDataSource<Int, Organization>() {
        override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Organization>) {
            callback.onResult(generateListOfMockOrganizations(10), null, null)
        }

        override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Organization>) {
            // not needed
        }

        override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Organization>) {
            // not needed
        }
    }
}