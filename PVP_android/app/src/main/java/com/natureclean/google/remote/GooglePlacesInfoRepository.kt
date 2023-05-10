package com.natureclean.google.remote

import com.natureclean.api.model.Resource
import com.natureclean.google.domain.model.GooglePlacesInfo
import kotlinx.coroutines.flow.Flow

interface GooglePlacesInfoRepository {
    fun getDirection(origin: String, destination: String, key: String): Flow<Resource<GooglePlacesInfo>>
}