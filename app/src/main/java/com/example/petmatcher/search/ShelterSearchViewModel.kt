package com.example.petmatcher.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.network.organizations.Organization
import com.example.network.shelter.Shelter
import com.example.petmatcher.data.ShelterRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShelterSearchViewModel @Inject constructor(private val shelterRepository: ShelterRepository)
    : ViewModel() {
    private val organizationSourceFactory = OrganizationDataSourceFactory(shelterRepository)
    var sheltersList: LiveData<PagedList<Organization>>

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(25)
            .setEnablePlaceholders(false)
            .build()

        sheltersList  = LivePagedListBuilder<String, Organization>(organizationSourceFactory, config).build()
    }

    fun refresh() {
        organizationSourceFactory.organizationSource.value?.invalidate()
    }

    @ExperimentalCoroutinesApi
    override fun onCleared() {
        super.onCleared()
    }
}