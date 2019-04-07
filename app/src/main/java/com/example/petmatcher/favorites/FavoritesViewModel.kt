package com.example.petmatcher.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.network.petlist.Pet
import com.example.petmatcher.data.Favorite
import com.example.petmatcher.data.FavoritesRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoritesViewModel @Inject constructor(favoritesRepository: FavoritesRepository): ViewModel() {
    val favorites = favoritesRepository.getFavoritesList()
}