package com.example.olx.users.repository

import com.example.olx.users.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.UUID

interface UserRepository :
    JpaRepository<User, UUID>,
    JpaSpecificationExecutor<User> {
    fun existsByUsername(username: String): Boolean
}
