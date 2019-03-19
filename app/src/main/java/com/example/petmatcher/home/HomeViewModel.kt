package com.example.petmatcher.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.network.petlist.Pet
import com.example.petmatcher.PetRepository
import com.example.petmatcher.favorites.FavoritesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for HomeFragment
 */
class HomeViewModel @Inject constructor(private val repository: PetRepository,
                                        private val favoritesRepository: FavoritesRepository): ViewModel() {
    val currentPet: MutableLiveData<Pet> = MutableLiveData()

    fun nextPet() {
        // todo fix paging
        if (repository.petList.isEmpty()) {
            repository.getPets()
        } else {
            currentPet.value = repository.petList.removeAt(0)
        }
    }

    @ExperimentalCoroutinesApi
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    fun addPetToFavorites(newPet: Pet) {
        viewModelScope.launch {
            favoritesRepository.addToFavorites(newPet)
        }
    }
}
