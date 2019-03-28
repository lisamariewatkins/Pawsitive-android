package com.example.petmatcher.search

import androidx.lifecycle.ViewModel
import com.example.petmatcher.data.ShelterRepository
import javax.inject.Inject

class ShelterSearchViewModel @Inject constructor(private val shelterRepository: ShelterRepository): ViewModel() {
    val shelters = shelterRepository.shelters
}