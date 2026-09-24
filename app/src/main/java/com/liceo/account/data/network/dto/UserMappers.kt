package com.liceo.account.data.network.dto

import com.liceo.account.domain.model.User

/** Convert the server DTO to the safe, screen-facing model. */
fun UserDto.toDomain(): User = User(
    id = id ?: "",
    fullName = fullname?.trim() ?: "(no name)",
    email = email?.trim() ?: "",
    birthdate = birthdate ?: "(not set)"
)
