package com.example.olx.users.exceptions

import com.example.olx.Loggable
import com.example.olx.exceptions.dto.ErrorDTO
import com.example.olx.exceptions.dto.ErrorResponse
import com.example.olx.exceptions.dto.ErrorType
import com.example.olx.users.controller.UserController
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(assignableTypes = [UserController::class])
class UserExceptionHandler : Loggable {
    @ExceptionHandler(UsernameAlreadyExistsException::class)
    fun usernameAlreadyExistsException(
        ex: UsernameAlreadyExistsException,
        request: WebRequest,
    ): ResponseEntity<ErrorResponse> {
        val message = ex.message ?: "Username already exists exception"

        logger.error { message }

        val errorDTO =
            ErrorDTO(
                type = ErrorType.CONFLICT,
                message = message,
            )

        val errorResponse =
            ErrorResponse(
                path = request.getDescription(false),
                status = HttpStatus.CONFLICT.value(),
                errors = listOf(errorDTO),
            )

        return ResponseEntity(errorResponse, HttpStatus.CONFLICT)
    }
}
