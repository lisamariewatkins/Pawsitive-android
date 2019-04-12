package com.example.petmatcher.favorites

import androidx.lifecycle.ViewModel
import com.example.petmatcher.data.FavoritesRepository

import javax.inject.Inject

class FavoritesViewModel @Inject constructor(favoritesRepository: FavoritesRepository): ViewModel() {
    val favorites = favoritesRepository.getFavoritesList()
}