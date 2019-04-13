package com.example.petmatcher.home

import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.network.animals.Animal
import com.example.petmatcher.R
import com.example.petmatcher.petdetails.AnimalRepository
import com.example.petmatcher.favorites.FavoritesRepository
import com.example.petmatcher.data.NetworkState
import com.example.petmatcher.memorycache.ImageCache
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

/**
 * ViewModel for [HomeFragment]
 */
class HomeViewModel @Inject constructor(private val animalRepository: AnimalRepository,
                                        private val favoritesRepository: FavoritesRepository,
                                        private val imageCache: ImageCache
): ViewModel() {
    val currentAnimal = MutableLiveData<Animal>()
    val networkState = MutableLiveData<NetworkState>()

    @VisibleForTesting
    internal val animalList = LinkedList<Animal>()

    init {
        if (animalList.isEmpty()) {
            loadAnimals()
        }
    }

    //region I/O
    fun nextAnimal() {
        if (!animalList.isEmpty()) {
            currentAnimal.value = animalList.pollFirst()
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
            networkState.postValue(NetworkState.RUNNING)
            loadAnimalsAsync()
        }
    }

    // TODO: Revisit error handling
    private suspend fun loadAnimalsAsync() = withContext(Dispatchers.Default) {
        try {
            val animalResponse = animalRepository.getAnimalsAsync().await()
            // cache pets
            animalList.addAll(animalResponse.animals)
            // update current pet
            currentAnimal.postValue(animalList.pollFirst())
            // cache images
            imageCache.cacheImages(animalList)
            // update network state
            networkState.postValue(NetworkState.SUCCESS)
        } catch (e: Exception) {
            networkState.postValue(NetworkState.FAILURE)
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
    fun showAnimal(animal: Animal, petNameTextView: TextView, petDescriptionTextView: TextView, petImage: ImageView) {
        val petName = animal.name
        val petDescription = animal.description

        if (!animal.photos.isEmpty()) {
            val imageUrl = animal.photos[0].large
            loadImage(petImage, imageUrl, animal.id)
        }

        petNameTextView.text = petName
        petDescriptionTextView.text = petDescription
    }

    fun displayViewByNetworkState(petLayout: ScrollView, progressBar: ProgressBar, state: NetworkState) {
        petLayout.visibility = if (state == NetworkState.SUCCESS) View.VISIBLE else View.GONE
        progressBar.visibility = if (state == NetworkState.SUCCESS || state == NetworkState.FAILURE) View.GONE else View.VISIBLE

        if (state == NetworkState.FAILURE) {
            // Toast.makeText(context, getString(R.string.error_message), Toast.LENGTH_LONG).show()
        }
    }

    // TODO: Handle null images from server
    private fun loadImage(petImage: ImageView, imageUrl: String?, id: Int) {
        imageCache.getImage(id)?.let {
            petImage.setImageDrawable(it)
        } ?: run {
            val options = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_placeholder_image)
                .error(R.drawable.ic_placeholder_image)

            Glide.with(petImage.context).load(imageUrl).apply(options).into(petImage)
        }
    }
    //endregion
}
