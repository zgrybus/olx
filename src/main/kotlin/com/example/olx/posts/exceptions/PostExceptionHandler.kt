package com.example.olx.posts.exceptions

import com.example.olx.Loggable
import com.example.olx.exceptions.dto.ErrorDTO
import com.example.olx.exceptions.dto.ErrorResponse
import com.example.olx.exceptions.dto.ErrorType
import com.example.olx.posts.controller.PostController
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(assignableTypes = [PostController::class])
class PostExceptionHandler : Loggable {

    @ExceptionHandler(PostNotFoundException::class)
    fun handlePostNotFound(ex: PostNotFoundException, request: WebRequest): ResponseEntity<ErrorResponse> {
        logger.error { "Post not found: $ex" }

        val errorDTO = ErrorDTO(type = ErrorType.NOT_FOUND, message = ex.message ?: "Post not found")

        val errorResponse = ErrorResponse(
            errors = listOf(errorDTO),
            status = HttpStatus.NOT_FOUND.value(),
            path = request.getDescription(false)
        )

        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }
}