package com.example.petmatcher.petdetails

import androidx.lifecycle.ViewModel
import com.example.petmatcher.data.FavoritesRepository
import javax.inject.Inject

/**
 * ViewModel for pet details screen
 */
class DetailsViewModel @Inject constructor(private val favoritesRepository: FavoritesRepository): ViewModel() {
    fun getPet(id: String) = favoritesRepository.getFavorite(id)
}