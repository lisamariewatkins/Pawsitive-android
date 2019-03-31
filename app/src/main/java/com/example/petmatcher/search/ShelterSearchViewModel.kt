package com.example.petmatcher.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.network.shelter.Shelter
import com.example.petmatcher.data.ShelterRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShelterSearchViewModel @Inject constructor(private val shelterRepository: ShelterRepository): ViewModel() {
    val sheltersList = MutableLiveData<List<Shelter>>()

    init {
        getShelters()
    }

    fun getShelters(): LiveData<List<Shelter>> {
        viewModelScope.launch {
            val response = shelterRepository.getSheltersAsync().await()
            sheltersList.postValue(response.petFinder.shelters.shelterList)
        }
        return sheltersList
    }
}