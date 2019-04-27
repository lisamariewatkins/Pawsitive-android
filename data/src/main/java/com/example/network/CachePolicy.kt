package com.example.network

enum class CachingPolicy(val settings: String) {
    SHELTER_POLICY("public,max-age=2592000"),
    FORCE_REFRESH("no-cache"),
    CACHE_ONLY("only-if-cached")
}