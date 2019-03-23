package com.example.petmatcher

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.network.petlist.JsonResponse
import com.example.network.petlist.Pet
import com.example.network.petlist.PetManager
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handles all data operations for adoptable pets, abstracting these operations from the rest of the app.
 */
@Singleton
class PetRepository @Inject constructor() {
    @Inject
    lateinit var petManager: PetManager

    var petList = MutableLiveData<LinkedList<Pet>>()
    var currentPet = MutableLiveData<Pet>()

    /** Offset for network paging **/
    private var offset: String? = null

    suspend fun getNextPet(): LiveData<Pet>? {
        // todo paging
        petList.value?.let {
            if (it.isEmpty()) {
                getPets()
            } else {
                currentPet.value = it.pollFirst()
            }
            return currentPet
        }
        return null
    }

    /**
     * Retrieve list of pets asynchronously from network
     */
    suspend fun getPets() {
        val response: Deferred<JsonResponse> = petManager.getPetListAsync("78701", offset)
        try {
            val data = response.await()

            // update offset for paging
            offset = data.petFinder.lastOffset.value

            data.petFinder.pets.pet.let {
                withContext(Main) {
                    petList.value = LinkedList(it)
                    petList.value?.let {
                        currentPet.value = it.pollFirst()
                    }
                }
            }
        } catch (e: Exception) {
            // TODO("Error handling")
        }
    }
}