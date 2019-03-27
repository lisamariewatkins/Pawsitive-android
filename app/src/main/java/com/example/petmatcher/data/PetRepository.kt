package com.example.petmatcher.data

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.network.petlist.JsonResponse
import com.example.network.petlist.Pet
import com.example.network.petlist.PetManager
import com.example.petmatcher.memorycache.ImageCache
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handles all data operations for adoptable pets, abstracting these operations from the rest of the app.
 */
@Singleton
class PetRepository @Inject constructor(private val imageCache: ImageCache,
                                        private val petManager: PetManager) {
    var petCache = LinkedList<Pet>()
    var currentPet = MutableLiveData<Pet>()

    /** Offset for network paging **/
    @VisibleForTesting
    internal var offset: String? = null

    suspend fun getNextPet(): LiveData<Pet> {
        if (petCache.isEmpty()) {
            getPets()
        } else {
            currentPet.value = petCache.pollFirst()
        }
        return currentPet
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
                petCache.addAll(it)

                // cache images
                imageCache.cacheImages(petCache)

                withContext(Main) {
                    currentPet.value = petCache.pollFirst()
                }
            }
        } catch (e: Exception) {
            // TODO("Error handling")
        }
    }
}