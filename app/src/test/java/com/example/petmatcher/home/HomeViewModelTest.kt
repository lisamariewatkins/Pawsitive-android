package com.example.petmatcher.home

import android.widget.ImageView
import android.widget.TextView
import com.example.petmatcher.favorites.FavoritesRepository
import com.example.petmatcher.data.MockAnimalJsonResponse
import com.example.petmatcher.imageutil.ImageCache
import com.example.petmatcher.imageutil.ImageLoader
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

    val mockAnimalRepository: AnimalRepository = mockk()
    val mockFavoriteRepository: FavoritesRepository = mockk(relaxed = true)
    val mockImageCache: ImageCache = mockk(relaxed = true)
    val mockImageLoader: ImageLoader = mockk()

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
            val mockJsonData = CompletableDeferred(MockAnimalJsonResponse().with(10))

            every {
                mockAnimalRepository.getAnimalsAsync()
            } returns mockJsonData

            // Exercise
            viewModel = HomeViewModel(mockAnimalRepository, mockFavoriteRepository, mockImageCache, mockImageLoader)

            // Assert
            val expected = mockJsonData.getCompleted()

            viewModel.currentAnimal.observeForever {
                Assert.assertEquals(expected.animals.size - 1, viewModel.animalList.size)
                Assert.assertEquals(expected.animals[0], it)
            }

            coVerify { mockImageCache.cacheImages(any()) }
        }

        // todo handle network failure
    }

    @Nested
    inner class UserInterfaceMethods {
        @Test
        fun `Verify adding the current pet to favorites`() = runBlocking {
            // Arrange
            val mockJsonData = CompletableDeferred(MockAnimalJsonResponse().with(10))

            every {
                mockAnimalRepository.getAnimalsAsync()
            } returns mockJsonData

            viewModel = HomeViewModel(mockAnimalRepository, mockFavoriteRepository, mockImageCache, mockImageLoader)

            viewModel.currentAnimal.observeForever {
                // Exercise
                viewModel.addCurrentAnimalToFavorites()

                // Assert
                coVerify { mockFavoriteRepository.addToFavorites(it) }
            }
        }

        @Test
        fun `Verify show pet populates proper views with data`() {
            // Arrange
            val mockJsonData = CompletableDeferred(MockAnimalJsonResponse().with(10))

            every {
                mockAnimalRepository.getAnimalsAsync()
            } returns mockJsonData

            viewModel = HomeViewModel(mockAnimalRepository, mockFavoriteRepository, mockImageCache, mockImageLoader)

            val mockPetNameTextView: TextView = mockk(relaxed = true)
            val mockPetDescriptionTextView: TextView = mockk(relaxed = true)
            val mockPetImageView: ImageView = mockk(relaxed = true)

            // Exercise
            viewModel.currentAnimal.observeForever {
                viewModel.showAnimal(it, mockPetNameTextView, mockPetDescriptionTextView, mockPetImageView)

                // Assert
                verify {
                    mockPetDescriptionTextView.text = it.description
                    mockPetNameTextView.text = it.name
                }
            }
        }

        // todo empty photos test
    }
}