package com.example.petmatcher.networkutil

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class Result<T>(
    val pagedList: T,
    val networkState: LiveData<NetworkState>,
    val refresh: () -> Unit,
    val retry: () -> Unit
)