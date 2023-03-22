package com.natureclean.api.model

data class UserCredentials(
    val name: String ? = null,
    val email: String,
    val password: String,
    val password_confirmation: String? = null
)
