package com.example.olx.auth.service

import com.example.olx.Loggable
import com.example.olx.auth.dto.LoginRequestDTO
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.context.SecurityContextRepository
import org.springframework.stereotype.Service

@Service
class AuthService(
    val securityContextRepository: SecurityContextRepository,
    val authenticationManager: AuthenticationManager,
) : Loggable {
    fun login(
        loginRequestDTO: LoginRequestDTO,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): ResponseEntity<String> {
        logger.info { "Attempt to login user: ${loginRequestDTO.username}" }

        val authentication =
            authenticationManager
                .authenticate(
                    UsernamePasswordAuthenticationToken(loginRequestDTO.username, loginRequestDTO.password),
                )

        val context = SecurityContextHolder.createEmptyContext()
        context.authentication = authentication
        SecurityContextHolder.setContext(context)
        securityContextRepository.saveContext(context, request, response)

        return ResponseEntity
            .ok("Logged in")
            .also {
                logger.info { "Successfully logged in user: ${loginRequestDTO.username}" }
            }
    }
}
