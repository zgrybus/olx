package com.example.olx.users.mappers

import com.example.olx.users.dto.UserDetailsDTO
import com.example.olx.users.entity.User

fun User.toUserDetailsDTO(): UserDetailsDTO =
    UserDetailsDTO(
        id = this.id!!,
        username = this.username,
    )
