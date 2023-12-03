package ru.bahusdivus.bilgeadamtest.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.bahusdivus.bilgeadamtest.controllers.dto.Response
import ru.bahusdivus.bilgeadamtest.services.EvaluationService

@RestController
@RequestMapping("/evaluation")
class EvaluationController(
    private val evaluationService: EvaluationService
) {

    @GetMapping
    fun getEvaluation(@RequestParam allParams: Map<String, String>): Response {
        val evaluation = evaluationService.getEvaluation(allParams)
        return evaluation
    }
}