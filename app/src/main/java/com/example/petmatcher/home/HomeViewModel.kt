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
import com.example.network.petlist.Pet
import com.example.petmatcher.R
import com.example.petmatcher.data.PetRepository
import com.example.petmatcher.data.FavoritesRepository
import com.example.petmatcher.memorycache.ImageCache
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * ViewModel for HomeFragment
 */
class HomeViewModel @Inject constructor(private val petRepository: PetRepository,
                                        private val favoritesRepository: FavoritesRepository,
                                        private val imageCache: ImageCache
): ViewModel() {

    val currentPet = MutableLiveData<Pet>()

    @VisibleForTesting
    internal val petList = LinkedList<Pet>()
    @VisibleForTesting
    internal var offset: String? = null

    init {
        if (petList.isEmpty()) {
            loadPets()
        }
    }

    fun nextPet() {
        if (!petList.isEmpty()) {
            currentPet.value = petList.pollFirst()
        } else {
            loadPets()
        }
    }

    fun addCurrentPetToFavorites() {
        viewModelScope.launch {
            currentPet.value?.let {
                favoritesRepository.addToFavorites(it)
            }
        }
    }

    private fun loadPets() {
        viewModelScope.launch {
            // todo error handling
            try {
                val pets = petRepository.getPetsAsync(offset).await()
                // update offset
                offset = pets.petFinder.lastOffset.value
                // cache pets
                petList.addAll(pets.petFinder.pets.pet)
                // update current pet
                currentPet.postValue(petList.pollFirst())
                // cache images
                imageCache.cacheImages(petList)
            } catch (e: Exception) {
                Log.e("HomeViewModel", e.message)
            }
        }
    }

    fun showPet(pet: Pet, petNameTextView: TextView, petDescriptionTextView: TextView, petImage: ImageView) {
        val petName = pet.name.value
        val petDescription = pet.description.value
        pet.media.photos?.let {
            val imageUrl = it.photoList[3].url
            loadImage(petImage, imageUrl, pet.id.value)
        }

        petNameTextView.text = petName
        petDescriptionTextView.text = petDescription
    }

    private fun loadImage(petImage: ImageView, imageUrl: String?, id: String) {
        imageCache.getImage(id)?.let {
            petImage.setImageDrawable(it)
        } ?: run {
            val options = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_placeholder_image) // todo find a placeholder asset
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
