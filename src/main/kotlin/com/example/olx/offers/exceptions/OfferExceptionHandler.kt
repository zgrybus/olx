package com.example.olx.offers.exceptions

import com.example.olx.Loggable
import com.example.olx.exceptions.dto.ErrorDTO
import com.example.olx.exceptions.dto.ErrorResponse
import com.example.olx.exceptions.dto.ErrorType
import com.example.olx.offers.controller.OfferController
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(assignableTypes = [OfferController::class])
class OfferExceptionHandler : Loggable {
    @ExceptionHandler(OfferNotFoundException::class)
    fun handleOfferNotFound(
        ex: OfferNotFoundException,
        request: WebRequest,
    ): ResponseEntity<ErrorResponse> {
        logger.error { "Offer not found: $ex" }

        val errorDTO = ErrorDTO(type = ErrorType.NOT_FOUND, message = ex.message ?: "Offer not found")

        val errorResponse =
            ErrorResponse(
                errors = listOf(errorDTO),
                status = HttpStatus.NOT_FOUND.value(),
                path = request.getDescription(false),
            )

        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleOfferNotValid(
        ex: MethodArgumentNotValidException,
        request: WebRequest,
    ): ResponseEntity<ErrorResponse> {
        logger.error { "Offer not valid: $ex" }

        val errorsDTO =
            ex.fieldErrors.map {
                ErrorDTO(type = ErrorType.NOT_VALID, message = it.defaultMessage ?: "Argument is not valid")
            }

        val errorResponse =
            ErrorResponse(
                errors = errorsDTO,
                status = HttpStatus.BAD_REQUEST.value(),
                path = request.getDescription(false),
            )

        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }
}
