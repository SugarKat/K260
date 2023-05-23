package com.natureclean.api.model

data class User(
    val user: UserObj,
    val token: String,
    val roles: List<String>, //user - roles[0] == "user", admin 0 roles[0] == "writer", roles[1] == "admin"
)
