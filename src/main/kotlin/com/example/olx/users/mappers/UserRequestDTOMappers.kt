package com.example.olx.users.mappers

import com.example.olx.users.dto.UserRequestDTO
import com.example.olx.users.entity.User

fun UserRequestDTO.toUser(password: String): User =
    User(
        username = this.username!!,
        password = password,
    )
