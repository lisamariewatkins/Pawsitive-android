package com.example.network

data class Result<T>(
    val pagedList: T,
    val refresh: () -> Unit,
    val retry: () -> Unit
)