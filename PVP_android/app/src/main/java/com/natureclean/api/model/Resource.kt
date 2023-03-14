package com.natureclean.api.model

sealed class Resource<T>(val data: T? = null, val message: String? = null, val error: com.natureclean.api.model.Error? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, error: com.natureclean.api.model.Error? = null, data: T? = null) : Resource<T>(data, message, error)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}

