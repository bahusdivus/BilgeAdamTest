package ru.bahusdivus.bilgeadamtest.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.ErrorResponse
import org.springframework.web.ErrorResponseException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.bahusdivus.bilgeadamtest.controllers.dto.Response
import ru.bahusdivus.bilgeadamtest.exceptions.UnprocessableCsvException
import ru.bahusdivus.bilgeadamtest.exceptions.UnreachableUrlException
import ru.bahusdivus.bilgeadamtest.services.EvaluationService
import java.net.MalformedURLException
import java.time.format.DateTimeParseException


@RestController
@RequestMapping("/evaluation")
class EvaluationController(
    private val evaluationService: EvaluationService,
) {

    @GetMapping
    fun getEvaluation(@RequestParam allParams: Map<String, String>): Response {
        return evaluationService.getEvaluation(allParams)
    }

    // All handlers should be moved to a separate controller advice class and proper custom exception used
    @ExceptionHandler(MalformedURLException::class)
    fun handleMalformedUrl(ex: MalformedURLException): ResponseEntity<ErrorResponse?>? {
        val errorResponse = ErrorResponseException(HttpStatus.BAD_REQUEST, ex)
        return ResponseEntity.badRequest().body(errorResponse)
    }

    @ExceptionHandler(UnreachableUrlException::class)
    fun handleMalformedUrl(ex: UnreachableUrlException): ResponseEntity<ErrorResponse?>? {
        val errorResponse = ErrorResponseException(HttpStatus.BAD_REQUEST, ex)
        return ResponseEntity.badRequest().body(errorResponse)
    }

    @ExceptionHandler(UnprocessableCsvException::class)
    fun handleMalformedUrl(ex: UnprocessableCsvException): ResponseEntity<ErrorResponse?>? {
        val errorResponse = ErrorResponseException(HttpStatus.UNPROCESSABLE_ENTITY, ex)
        return ResponseEntity.unprocessableEntity().body(errorResponse)
    }

    @ExceptionHandler(DateTimeParseException::class)
    fun handleMalformedUrl(ex: DateTimeParseException): ResponseEntity<ErrorResponse?>? {
        val errorResponse = ErrorResponseException(HttpStatus.UNPROCESSABLE_ENTITY, ex)
        return ResponseEntity.unprocessableEntity().body(errorResponse)
    }

    @ExceptionHandler(NumberFormatException::class)
    fun handleMalformedUrl(ex: NumberFormatException): ResponseEntity<ErrorResponse?>? {
        val errorResponse = ErrorResponseException(HttpStatus.UNPROCESSABLE_ENTITY, ex)
        return ResponseEntity.unprocessableEntity().body(errorResponse)
    }
}