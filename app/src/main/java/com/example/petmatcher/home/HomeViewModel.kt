package com.example.petmatcher.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.network.petlist.Pet
import com.example.petmatcher.data.PetRepository
import com.example.petmatcher.data.FavoritesRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for HomeFragment
 */
class HomeViewModel @Inject constructor(private val repository: PetRepository,
                                        private val favoritesRepository: FavoritesRepository
): ViewModel() {

    val currentPet = repository.currentPet

    fun nextPet() {
        viewModelScope.launch {
            repository.getNextPet()
        }
    }

    fun addPetToFavorites(newPet: Pet) {
        viewModelScope.launch {
            favoritesRepository.addToFavorites(newPet)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}
