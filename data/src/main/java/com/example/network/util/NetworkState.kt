package com.example.network.util

enum class ErrorState {
    NO_NETWORK,
    NETWORK_FAILURE,
    EMPTY_RESPONSE,
    UNKNOWN
}

enum class NetworkState {
    RUNNING,
    SUCCESS,
    FAILURE
}