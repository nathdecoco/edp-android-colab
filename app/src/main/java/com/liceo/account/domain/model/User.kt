package com.liceo.account.domain.model

/** The user shape used by the screens. It intentionally has no password. */
data class User(
    val id: String,
    val fullName: String,
    val email: String,
    val birthdate: String
)
