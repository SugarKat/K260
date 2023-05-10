package com.natureclean.google.remote.dto

import com.natureclean.google.domain.model.Distance

data class DistanceDto(
    val text: String,
    val value: Int
){
    fun toDistance(): Distance{
        return  Distance(
            text = text,
            value = value
        )
    }
}