package com.example.petmatcher.home

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.network.animals.Animal
import com.example.petmatcher.R
import com.example.petmatcher.data.AnimalRepository
import com.example.petmatcher.data.FavoritesRepository
import com.example.petmatcher.errorhandling.ErrorState
import com.example.petmatcher.memorycache.ImageCache
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * ViewModel for HomeFragment
 */
class HomeViewModel @Inject constructor(private val animalRepository: AnimalRepository,
                                        private val favoritesRepository: FavoritesRepository,
                                        private val imageCache: ImageCache
): ViewModel() {
    val currentAnimal = MutableLiveData<Animal>()
    val error = MutableLiveData<ErrorState>()

    @VisibleForTesting
    internal val animalList = LinkedList<Animal>()

    init {
        if (animalList.isEmpty()) {
            loadAnimals()
        }
    }

    fun nextAnimal() {
        if (!animalList.isEmpty()) {
            currentAnimal.value = animalList.pollFirst()
        } else {
            loadAnimals()
        }
    }

    fun addCurrentAnimalToFavorites() {
        viewModelScope.launch {
            currentAnimal.value?.let {
                favoritesRepository.addToFavorites(it)
            }
        }
    }

    private fun loadAnimals() {
        viewModelScope.launch {
            try {
                val animalResponse = animalRepository.getAnimalsAsync().await()
                // cache pets
                animalList.addAll(animalResponse.animals)
                // update current pet
                currentAnimal.postValue(animalList.pollFirst())
                // cache images
                imageCache.cacheImages(animalList)
            } catch (e: Exception) {
                error.postValue(ErrorState.NETWORK_FAILURE)
                Log.e("HomeViewModel", e.message)
            }
        }
    }

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

    @ExperimentalCoroutinesApi
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}
