package com.example.olx.auth.service

import com.example.olx.auth.dto.CustomAuthUserDetailsDTO
import com.example.olx.users.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomAuthUserDetailsService(
    val userRepository: UserRepository,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): CustomAuthUserDetailsDTO {
        val user =
            userRepository.findByUsername(username)
                ?: throw UsernameNotFoundException("User $username not found")

        return CustomAuthUserDetailsDTO(
            id = user.id!!,
            username = user.username,
            password = user.password,
            authorities = listOf(),
        )
    }
}
