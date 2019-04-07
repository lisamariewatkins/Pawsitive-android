package com.example.petmatcher.home

import android.widget.ImageView
import android.widget.TextView
import com.example.network.petlist.Pet
import com.example.petmatcher.data.FavoritesRepository
import com.example.petmatcher.data.MockPetJsonResponse
import com.example.petmatcher.data.PetRepository
import com.example.petmatcher.memorycache.ImageCache
import com.example.petmatcher.testextensions.InstantExecutorExtension
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.concurrent.Executors

@ExtendWith(InstantExecutorExtension::class)
class HomeViewModelTest {

    val mockPetRepository: PetRepository = mockk()
    val mockFavoriteRepository: FavoritesRepository = mockk(relaxed = true)
    val mockImageCache: ImageCache = mockk(relaxed = true)

    lateinit var viewModel: HomeViewModel

    @BeforeEach
    @ExperimentalCoroutinesApi
    fun setUp() {
        Dispatchers.setMain(Executors.newSingleThreadExecutor().asCoroutineDispatcher())
    }

    @AfterEach
    @ExperimentalCoroutinesApi
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Nested
    inner class Initialization {
        /**
         * Verify that a [HomeViewModel] is initialized with a list of pets, updates the current pet to the first pet
         * in the list, and calls [ImageCache] to pre-cache all the images
         */
        @Test
        @ExperimentalCoroutinesApi
        fun `Verify ViewModel is initialized with pet list if server request successful`() = runBlocking {
            // Arrange
            val mockJsonData = CompletableDeferred(MockPetJsonResponse().with(10))

            every {
                mockPetRepository.getPetsAsync(any())
            } returns mockJsonData

            // Exercise
            viewModel = HomeViewModel(mockPetRepository, mockFavoriteRepository, mockImageCache)

            // Assert
            val expected = mockJsonData.getCompleted()

            viewModel.currentPet.observeForever {
                Assert.assertEquals(expected.petFinder.pets.pet.size - 1, viewModel.petList.size)
                Assert.assertEquals(expected.petFinder.pets.pet[0], it)
            }

            coVerify {
                mockImageCache.cacheImages(any())
            }
        }

        // todo handle network failure
    }

    @Nested
    inner class UserInterfaceMethods {
        @Test
        fun `Verify adding the current pet to favorites`() = runBlocking {
            // Arrange
            val mockJsonData = CompletableDeferred(MockPetJsonResponse().with(10))

            every {
                mockPetRepository.getPetsAsync(any())
            } returns mockJsonData

            viewModel = HomeViewModel(mockPetRepository, mockFavoriteRepository, mockImageCache)

            viewModel.currentPet.observeForever {
                // Exercise
                viewModel.addCurrentPetToFavorites()

                // Assert
                coVerify {
                    mockFavoriteRepository.addToFavorites(it)
                }
            }
        }

        @Test
        fun `Verify show pet populates proper views with data`() {
            // Arrange
            val mockJsonData = CompletableDeferred(MockPetJsonResponse().with(10))

            every {
                mockPetRepository.getPetsAsync(any())
            } returns mockJsonData

            viewModel = HomeViewModel(mockPetRepository, mockFavoriteRepository, mockImageCache)

            val mockPetNameTextView: TextView = mockk(relaxed = true)
            val mockPetDescriptionTextView: TextView = mockk(relaxed = true)
            val mockPetImageView: ImageView = mockk(relaxed = true)

            // Exercise
            viewModel.currentPet.observeForever {
                viewModel.showPet(it, mockPetNameTextView, mockPetDescriptionTextView, mockPetImageView)

                // Assert
                verify {
                    mockPetDescriptionTextView.text = it.description.value
                    mockPetNameTextView.text = it.name.value
                }
            }
        }

        // todo empty photos test
    }
}