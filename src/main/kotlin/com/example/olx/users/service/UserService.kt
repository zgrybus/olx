package com.example.olx.users.service

import com.example.olx.Loggable
import com.example.olx.users.dto.UserDetailsDTO
import com.example.olx.users.dto.UserRequestDTO
import com.example.olx.users.exceptions.UsernameAlreadyExistsException
import com.example.olx.users.mappers.toUser
import com.example.olx.users.mappers.toUserDetailsDTO
import com.example.olx.users.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder,
) : Loggable {
    @Transactional
    fun createUser(userRequestDTO: UserRequestDTO): UserDetailsDTO {
        logger.info { "Attempt to create user ${userRequestDTO.username!!}" }

        if (userRepository.existsByUsername(userRequestDTO.username!!)) {
            throw UsernameAlreadyExistsException("Username already taken")
        }

        val encodedPassword = passwordEncoder.encode(userRequestDTO.password)

        return userRepository
            .save(
                userRequestDTO.toUser(encodedPassword),
            ).also {
                logger.info { "User ${userRequestDTO.username} created" }
            }.toUserDetailsDTO()
    }
}
