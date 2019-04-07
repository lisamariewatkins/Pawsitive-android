package com.example.petmatcher.data

import com.example.network.petlist.PetJsonResponse
import com.example.network.petlist.PetManager
import kotlinx.coroutines.Deferred
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handles all data operations for adoptable pets, abstracting these operations from the rest of the app.
 */
@Singleton
class PetRepository @Inject constructor(private val petManager: PetManager) {

    /** Retrieve a list of pets asynchronously from the network **/
    fun getPetsAsync(offset: String?): Deferred<PetJsonResponse> {
        return petManager.getPetListAsync("78701", offset)
    }
}