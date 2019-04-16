package com.example.petmatcher.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class Result<T>(
    val pagedList: LiveData<PagedList<T>>,
    val networkState: LiveData<NetworkState>,
    val refresh: () -> Unit
)