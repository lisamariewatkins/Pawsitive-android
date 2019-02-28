package com.example.petmatcher.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.network.petlist.Pet
import com.example.petmatcher.PetRepository

class HomeViewModel : ViewModel() {
    // todo add dagger
    private val repository = PetRepository()

    fun getPet(): LiveData<Pet> {
        return repository.getNextPet()
    }
}
