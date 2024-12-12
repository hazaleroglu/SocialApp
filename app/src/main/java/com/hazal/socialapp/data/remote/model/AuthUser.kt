package com.hazal.socialapp.data.remote.model

data class AuthUser(
    val name: String? = "",
    val email: String? = "",
    val id: String? = "",
    val isAnonymous: Boolean? = true
)

data class User(
    val firstName: String? = "",
    val lastName: String? = "",
)