package com.example.network.util

data class Result<T>(
    val pagedList: T,
    val refresh: () -> Unit,
    val retry: () -> Unit
)