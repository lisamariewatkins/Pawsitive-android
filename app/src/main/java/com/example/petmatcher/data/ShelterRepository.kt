package com.example.petmatcher.data

import androidx.lifecycle.MutableLiveData
import com.example.network.shelter.Shelter
import com.example.network.shelter.ShelterManager
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShelterRepository @Inject constructor(private val shelterManager: ShelterManager) {
    val shelters = MutableLiveData<List<Shelter>>()

    suspend fun getShelters() {
        val response = shelterManager.getShelterListAsync("78701")

        withContext(IO) {
            try {
                val data = response.await()
                // post shelter list on main thread
                shelters.postValue(data.petFinder.shelters.shelterList)
            } catch (exception: Exception){
                // TODO
            }
        }
    }
}