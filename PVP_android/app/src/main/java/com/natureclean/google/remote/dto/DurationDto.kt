package com.natureclean.google.remote.dto

import com.natureclean.google.domain.model.Duration

data class DurationDto(
    val text: String,
    val value: Int
){
    fun toDuration(): Duration {
        return Duration(
            text = text,
            value = value
        )
    }
}