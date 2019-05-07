package com.example.repository

import androidx.lifecycle.LiveData
import com.example.network.util.NetworkState

interface OrganizationRepository<T> {
    fun getOrganizations(): LiveData<T>
    fun getNetworkState(): LiveData<NetworkState>
}