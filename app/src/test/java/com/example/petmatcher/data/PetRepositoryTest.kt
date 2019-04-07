package com.example.petmatcher.data

import com.example.network.petlist.PetManager
import io.mockk.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

/**
 * Verifies functionality of [PetManager]
 *
 * Note: We've opted for Mockk because it supports final classes by default.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PetRepositoryTest {
    /** Mocks **/
    private val mockPetManager: PetManager = mockk()

    private val petRepository = PetRepository(mockPetManager)

    /** Verify getNextPet works as expected **/
    @Nested
    inner class GetNextPet {

        @Test
        @ExperimentalCoroutinesApi
        fun `verify method returns result and offset`() = runBlocking {
            // Arrange
            val deferred = CompletableDeferred(MockPetJsonResponse().with(1))

            every {
                mockPetManager.getPetListAsync("78701", null)
            } returns deferred

            // Exercise
            val result = petRepository.getPetsAsync(null).await()
            val offset = result.petFinder.lastOffset
            val pets = result.petFinder.pets.pet

            // Assert
            val expected = deferred.getCompleted()

            Assert.assertEquals(expected.petFinder.pets.pet, pets) // Assert the list we expect is of equal size
            Assert.assertEquals(expected.petFinder.lastOffset, offset)
        }
    }
}