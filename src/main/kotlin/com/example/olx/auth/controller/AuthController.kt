package com.example.olx.auth.controller

import com.example.olx.auth.dto.LoginRequestDTO
import com.example.olx.auth.service.AuthService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    val authService: AuthService,
) {
    @PostMapping("/login")
    fun login(
        @RequestBody loginRequestDTO: LoginRequestDTO,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): ResponseEntity<String> = authService.login(loginRequestDTO, request, response)
}
