package com.example.petmatcher.petdetails

import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.database.Favorite
import com.example.petmatcher.favorites.FavoritesRepository
import com.example.petmatcher.imageutil.ImageCache
import com.example.petmatcher.imageutil.ImageLoader
import com.example.network.util.ErrorState
import javax.inject.Inject

/**
 * ViewModel for pet details screen
 */
class DetailsViewModel @Inject constructor(private val favoritesRepository: FavoritesRepository,
                                           private val imageLoader: ImageLoader,
                                           private val imageCache: ImageCache)
    : ViewModel() {

    private val _errorState = MutableLiveData<ErrorState>()

    val errorState: LiveData<ErrorState>
        get() = _errorState

    fun getPet(id: Int) = favoritesRepository.getFavorite(id)

    fun loadPet(petImageView: ImageView, petNameTextView: TextView, petDescriptionTextView: TextView, favorite: Favorite?) {
        favorite?.let {
            petNameTextView.text = favorite.name
            petDescriptionTextView.text = favorite.description

            loadImage(
                petId = favorite.petId,
                imageUrl = favorite.imageUrl,
                imageView = petImageView)
        } ?: run {
            _errorState.value = ErrorState.UNKNOWN
        }
    }

    private fun loadImage(petId: Int, imageUrl: String, imageView: ImageView) {
        imageCache.getImage(petId)?.let {image ->
            imageView.setImageDrawable(image)
        } ?: run {
            imageLoader.loadImageIntoView(
                url = imageUrl,
                imageView = imageView)
        }
    }
}