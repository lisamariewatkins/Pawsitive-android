package com.example.petmatcher.networkutil

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