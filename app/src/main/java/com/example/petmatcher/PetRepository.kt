package com.example.petmatcher

import com.example.network.petlist.JsonResponse
import com.example.network.petlist.Pet
import com.example.network.petlist.PetManager
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Handles all data operations for adoptable pets, abstracting these operations from the rest of the app.
 */
class PetRepository @Inject constructor() {
    @Inject
    lateinit var petManager: PetManager

    // todo make a better in mem cache
    var petList = ArrayList<Pet>()

    /**
     * Retrieve list of pets asynchronously from network
     */
    fun getPets() {
        // TODO("Coroutines scope")
        GlobalScope.launch {
            val response: Deferred<JsonResponse> = petManager.getPetList("78701")
            try {
                response.await().petFinder.pets.pet.let {
                    petList.addAll(it)
                }
            } catch (e: Exception) {
                // TODO("Error handling")
            }
        }
    }
}