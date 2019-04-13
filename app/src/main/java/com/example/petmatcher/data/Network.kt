package com.example.petmatcher.data

enum class ErrorState {
    NO_NETWORK,
    NETWORK_FAILURE,
    EMPTY_RESPONSE
}

enum class NetworkState {
    RUNNING,
    SUCCESS,
    FAILURE
}