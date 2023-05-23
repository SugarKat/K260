package com.natureclean.api.model

data class UserObj(
    val id: String,
    val name: String,
    val email: String,
    val email_verified_at: String? = null,
    val points: Int = 0,
    val created_at: String,
    val updated_at: String,
    val distance_travelled: Int = 0,
)
