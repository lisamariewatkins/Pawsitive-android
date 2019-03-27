package com.example.petmatcher.data

import com.example.network.petlist.PetManager
import com.example.petmatcher.InstantExecutorExtension
import com.example.petmatcher.memorycache.ImageCache
import io.mockk.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Verifies functionality of [PetManager]
 *
 * Note: We've opted for Mockk because it supports final classes by default.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(InstantExecutorExtension::class)
class PetRepositoryTest {
    /** Mocks **/
    private val mockPetManager: PetManager = mockk()
    private val mockImageCache: ImageCache = mockk()

    private val petRepository = PetRepository(mockImageCache, mockPetManager)

    /** Verify getNextPet works as expected **/
    @Nested
    inner class GetNextPet {

        @Test
        @ExperimentalCoroutinesApi
        fun `verify method returns result, caches images, and updates offset`() = runBlocking {
            // Arrange
            val deferred = CompletableDeferred(MockJsonResponse().with(1))

            every {
                mockPetManager.getPetListAsync("78701", null)
            } returns deferred

            // Exercise
            val result = petRepository.getNextPet()

            // Assert
            result.observeForever {
                Assert.assertNotNull(it)
            }
            coVerify {
                mockImageCache.cacheImages(any())
            }
            Assert.assertEquals(deferred.getCompleted().petFinder.lastOffset.value, petRepository.offset)
        }
    }
}