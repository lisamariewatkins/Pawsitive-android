package com.example.petmatcher.search

import androidx.paging.PageKeyedDataSource
import com.example.network.shelter.Shelter
import com.example.petmatcher.data.ShelterRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// todo network failure / no network
// todo dagger set up
// todo update with paginated v2 API
class ShelterDataSource constructor(private val shelterRepository: ShelterRepository)
    : PageKeyedDataSource<String, Shelter>() {


    // todo scope
    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, Shelter>) {
        GlobalScope.launch {
            val response = shelterRepository.getSheltersAsync(null).await()
            val shelters = response.petFinder.shelters.shelterList

            val next = response.petFinder.lastOffset.value
            val previous = (next.toInt() - 25).toString()

            callback.onResult(shelters, previous, next)
        }
    }

    // todo scope
    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Shelter>) {
        GlobalScope.launch {
            val response = shelterRepository.getSheltersAsync(null).await()
            val shelters = response.petFinder.shelters.shelterList

            val next = response.petFinder.lastOffset.value

            callback.onResult(shelters, next)
        }
    }

    // todo scope
    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, Shelter>) {
        GlobalScope.launch {
            val response = shelterRepository.getSheltersAsync(null).await()
            val shelters = response.petFinder.shelters.shelterList

            val next = response.petFinder.lastOffset.value
            val previous = (next.toInt() - 25).toString()

            callback.onResult(shelters, previous)
        }
    }
}