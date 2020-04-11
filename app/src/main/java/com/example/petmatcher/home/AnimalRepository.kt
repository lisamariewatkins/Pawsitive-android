package com.example.petmatcher.home

import android.util.Log
import com.example.models.AnimalJsonResponse
import com.example.network.AnimalService
import kotlinx.coroutines.Deferred
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handles all data operations for adoptable animals, abstracting these operations from the rest of the app.
 */
@Singleton
class AnimalRepository @Inject constructor(private val animalService: AnimalService) {

    /** Retrieve a list of animals asynchronously from the network **/
    fun getAnimalsAsync(page: Int): Deferred<AnimalJsonResponse> {
        Log.d("AnimalRepo", "Launching network request from " + Thread.currentThread().name)
        return animalService.getAnimalsAsync(page)
    }
}