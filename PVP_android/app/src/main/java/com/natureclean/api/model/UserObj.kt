package com.natureclean.api.model

data class UserObj(
    val id: Int,
    val name: String,
    val email: String,
    val email_verified_at: String? = null,
    val created_at: String,
    val updated_at: String
)
