package com.example.petmatcher.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.network.shelter.Shelter
import com.example.petmatcher.data.ShelterRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShelterSearchViewModel @Inject constructor(private val shelterRepository: ShelterRepository)
    : ViewModel() {
    var sheltersList: LiveData<PagedList<Shelter>>

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(25)
            .setEnablePlaceholders(false)
            .build()

        sheltersList  = initializedPagedListBuilder(config).build()
    }

    private fun initializedPagedListBuilder(config: PagedList.Config): LivePagedListBuilder<String, Shelter> {

        val dataSourceFactory = object : DataSource.Factory<String, Shelter>() {
            override fun create(): DataSource<String, Shelter> {
                return ShelterDataSource(shelterRepository)
            }
        }
        return LivePagedListBuilder<String, Shelter>(dataSourceFactory, config)
    }

    @ExperimentalCoroutinesApi
    override fun onCleared() {
        super.onCleared()
    }
}