package com.example.petmatcher.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.network.petlist.Pet
import com.example.petmatcher.PetRepository
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val repository: PetRepository): ViewModel() {
    fun getPet(): LiveData<Pet> {
        return repository.getNextPet()
    }
}
