package com.example.olx.users.controller

import com.example.olx.users.dto.UserDetailsDTO
import com.example.olx.users.dto.UserRequestDTO
import com.example.olx.users.service.UserService
import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
@Validated
class UserController(
    val userService: UserService,
) {
    @PostMapping("/register")
    fun createUser(
        @Valid user: UserRequestDTO,
    ): UserDetailsDTO = userService.createUser(user)
}
