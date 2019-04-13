package com.example.petmatcher.petdetails

import android.util.Log
import com.example.network.animals.AnimalJsonResponse
import com.example.network.animals.AnimalService
import kotlinx.coroutines.Deferred
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handles all data operations for adoptable animals, abstracting these operations from the rest of the app.
 */
@Singleton
class AnimalRepository @Inject constructor(private val animalService: AnimalService) {

    /** Retrieve a list of animals asynchronously from the network **/
    fun getAnimalsAsync(): Deferred<AnimalJsonResponse> {
        Log.d("AnimalRepo", "Launching network request from " + Thread.currentThread().name)
        return animalService.getAnimalsAsync()
    }
}