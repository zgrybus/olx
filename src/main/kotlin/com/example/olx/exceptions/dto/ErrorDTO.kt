package com.example.olx.exceptions.dto

enum class ErrorType {
    NOT_FOUND,
    NOT_VALID,
    SOMETHING_WENT_WRONG
}

data class ErrorDTO(
    val type: ErrorType,
    val message: String,
)

data class ErrorResponse(
    val path: String,
    val status: Int,
    val errors: List<ErrorDTO>
)