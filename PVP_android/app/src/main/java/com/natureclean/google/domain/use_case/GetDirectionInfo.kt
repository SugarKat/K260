package com.natureclean.google.domain.use_case

import com.natureclean.api.model.Resource
import com.natureclean.google.domain.model.GooglePlacesInfo
import com.natureclean.google.remote.GooglePlacesInfoRepository
import kotlinx.coroutines.flow.Flow

class GetDirectionInfo(private val repository: GooglePlacesInfoRepository) {
    operator fun invoke(origin: String, destination: String, key: String): Flow<Resource<GooglePlacesInfo>> = repository.getDirection(origin = origin, destination = destination, key = key)
}