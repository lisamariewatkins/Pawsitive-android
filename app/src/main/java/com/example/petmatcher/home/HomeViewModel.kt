package com.example.petmatcher.home

import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.models.Animal
import com.example.petmatcher.R
import com.example.petmatcher.favorites.FavoritesRepository
import com.example.network.util.NetworkState
import com.example.petmatcher.imageutil.ImageCache
import com.example.petmatcher.imageutil.ImageLoader
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

/**
 * ViewModel for [HomeFragment]
 */
class HomeViewModel @Inject constructor(private val animalRepository: AnimalRepository,
                                        private val favoritesRepository: FavoritesRepository,
                                        private val imageCache: ImageCache,
                                        private val imageLoader: ImageLoader
): ViewModel() {
    private val _currentAnimal = MutableLiveData<Animal>()
    val currentAnimal: LiveData<Animal>
        get() = _currentAnimal

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    @VisibleForTesting
    internal val animalList = LinkedList<Animal>()

    private var page = 1

    init {
        if (animalList.isEmpty()) {
            loadAnimals()
        }
    }

    //region I/O
    fun nextAnimal() {
        if (animalList.isNotEmpty()) {
            _currentAnimal.value = animalList.pollFirst()
        } else {
            loadAnimals()
        }
    }

    fun addCurrentAnimalToFavorites() {
        viewModelScope.launch {
            addAnimalToFavoritesAsync()
        }
    }

    private fun loadAnimals() {
        viewModelScope.launch {
            _networkState.postValue(NetworkState.RUNNING)
            loadAnimalsAsync()
        }
    }

    // TODO: Revisit error handling
    private suspend fun loadAnimalsAsync() = withContext(Dispatchers.Default) {
        try {
            val animalResponse = animalRepository.getAnimalsAsync(page++).await()
            // cache pets
            animalList.addAll(animalResponse.animals)
            // update current pet
            _currentAnimal.postValue(animalList.pollFirst())
            // cache images
            imageCache.cacheImages(animalList)
            // update network state
            _networkState.postValue(NetworkState.SUCCESS)
        } catch (e: Exception) {
            _networkState.postValue(NetworkState.FAILURE)
            Log.e("HomeViewModel", e.message)
        }
    }

    private suspend fun addAnimalToFavoritesAsync() = withContext(Dispatchers.IO) {
        currentAnimal.value?.let {
            favoritesRepository.addToFavorites(it)
        }
    }
    //endregion

    //region UI
    fun showAnimal(animal: Animal, petNameTextView: TextView, petDescriptionTextView: TextView, petImageView: ImageView) {
        if (animal.photos.isNotEmpty()) {
            // TODO: Will all responses have a large image?
            loadImage(petImageView = petImageView, imageUrl = animal.photos[0].large, id = animal.id)
        } else {
            petImageView.setImageResource(R.drawable.ic_placeholder_image)
        }

        petNameTextView.text = animal.name
        petDescriptionTextView.text = animal.description
    }

    // TODO: How can we simplify this?
    fun displayViewByNetworkState(petLayout: ScrollView, loadingLayout: FrameLayout, state: NetworkState) {
        when (state) {
            NetworkState.SUCCESS -> {
                petLayout.visibility = View.VISIBLE
                loadingLayout.visibility = View.GONE
            }
            NetworkState.RUNNING -> {
                petLayout.visibility = View.GONE
                loadingLayout.visibility = View.VISIBLE
                loadingLayout.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.VISIBLE
                loadingLayout.findViewById<LinearLayout>(R.id.network_error).visibility = View.GONE
            }
            NetworkState.FAILURE -> {
                petLayout.visibility = View.GONE
                loadingLayout.visibility = View.VISIBLE
                loadingLayout.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.GONE
                loadingLayout.findViewById<LinearLayout>(R.id.network_error).visibility = View.VISIBLE
            }
        }
    }

    private fun loadImage(petImageView: ImageView, imageUrl: String, id: Int) {
        imageCache.getImage(id)?.let {
            petImageView.setImageDrawable(it)
        } ?: run {
            imageLoader.loadImageIntoView(
                url = imageUrl,
                imageView = petImageView)
        }
    }
    //endregion
}
