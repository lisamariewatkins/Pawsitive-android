package com.example.petmatcher.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.network.petlist.Pet
import com.example.petmatcher.PetRepository
import javax.inject.Inject

/**
 * ViewModel for HomeFragment
 */
class HomeViewModel @Inject constructor(private val repository: PetRepository): ViewModel() {
    val currentPet: MutableLiveData<Pet> = MutableLiveData()

    fun nextPet() {
        // todo fix paging
        if (repository.petList.isEmpty()) {
            repository.getPets()
        } else {
            currentPet.value = repository.petList.removeAt(0)
        }
    }
}
