package com.example.petmatcher.home

import com.example.network.AnimalService
import com.example.petmatcher.data.MockAnimalJsonResponse
import io.mockk.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

/**
 * Verifies functionality of [AnimalRepository]
 *
 * Note: We've opted for Mockk because it supports final classes by default.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AnimalRepositoryTest {
    /** Mocks **/
    private val mockAnimalService: AnimalService = mockk()

    private val petRepository = AnimalRepository(mockAnimalService)

    /** Verify getNextPet works as expected **/
    @Nested
    inner class GetAnimals {

        @Test
        @ExperimentalCoroutinesApi
        fun `verify good network connection returns result`() = runBlocking {
            // Arrange
            val deferred = CompletableDeferred(MockAnimalJsonResponse().with(1))

            every {
                mockAnimalService.getAnimalsAsync()
            } returns deferred

            // Exercise
            val result = petRepository.getAnimalsAsync().await()

            // Assert
            val expected = deferred.getCompleted()
            Assert.assertEquals(expected.animals, result.animals)
        }

        // todo bad network connection
    }
}