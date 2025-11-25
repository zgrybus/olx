package com.example.olx.users.controller

import com.example.olx.users.dto.UserDetailsDTO
import com.example.olx.users.dto.UserRequestDTO
import com.example.olx.users.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
@Validated
class UserController(
    val userService: UserService,
) {
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(
        @RequestBody @Valid user: UserRequestDTO,
    ): UserDetailsDTO = userService.createUser(user)
}
