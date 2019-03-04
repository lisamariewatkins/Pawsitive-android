package com.example.petmatcher

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.network.petlist.JsonResponse
import com.example.network.petlist.Pet
import com.example.network.petlist.PetManager
import com.example.network.petlist.PetManagerImpl
import retrofit2.Response
import javax.inject.Inject

/*
* Handles all data operations for adoptable pets, abstracting these operations from the rest of the app.
*/
class PetRepository @Inject constructor() {
    @Inject
    lateinit var petManager: PetManager

    // todo make a better in mem cache
    private var petList = ArrayList<Pet>()

    fun getNextPet(): Pet? {
        // todo fix paging
        if (petList.isEmpty()) {
            getPets()
        }

        val data = MutableLiveData<Pet>()

        // todo use a better data structure
        if (petList.size > 0) {
            return petList.removeAt(0)
        } else {
            return null
        }
    }

    /*
    * Retrieve list of pets from the network
    */
    fun getPets() {
        PetTask(petList, petManager).execute()
    }

    // todo maybe coroutines instead
    /*
    * Request list of pets on background thread
    */
    private class PetTask(val petList: ArrayList<Pet>, val petManager: PetManager): AsyncTask<Void, Void, Response<JsonResponse>>() {
        override fun doInBackground(vararg params: Void?): Response<JsonResponse> {
            // todo location awareness
            return petManager.getPetList("78701").execute()
        }

        override fun onPostExecute(result: Response<JsonResponse>?) {
            result?.body()?.petFinder?.pets?.pet?.let {
                petList.addAll(it)
            }
        }
    }
}