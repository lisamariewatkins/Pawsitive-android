package com.example.petmatcher.data

import com.example.network.shelter.ShelterJsonResponse
import com.example.network.shelter.ShelterManager
import com.example.network.CachingPolicy
import kotlinx.coroutines.Deferred
import javax.inject.Inject

class ShelterRepository @Inject constructor(private val shelterManager: ShelterManager) {

    fun getSheltersAsync(offset: String?): Deferred<ShelterJsonResponse> {
        return shelterManager.getShelterListAsync("78701", offset, CachingPolicy.SHELTER_POLICY.settings)
    }
}