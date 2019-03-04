package com.example.petmatcher.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.network.petlist.Pet
import com.example.petmatcher.PetRepository
import javax.inject.Inject

/*
*
*/
class HomeViewModel @Inject constructor(private val repository: PetRepository): ViewModel() {
    val currentPet: MutableLiveData<Pet> = MutableLiveData()

    /*
    * Eagerly load pets
    */
    init {
        repository.getPets()
        currentPet.value = repository.getNextPet()
    }

    fun nextPet() {
        currentPet.value = repository.getNextPet()
    }
}
