package ru.bahusdivus.bilgeadamtest.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.core.io.ResourceLoader
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ru.bahusdivus.bilgeadamtest.BilgeAdamTestApplicationTests
import ru.bahusdivus.bilgeadamtest.controllers.dto.Response

@AutoConfigureMockMvc
class EvaluationControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val resourceLoader: ResourceLoader,
    val objectMapper: ObjectMapper,
) : BilgeAdamTestApplicationTests() {

    @Test
    fun `should return evaluation for multiple urls`() {
        val response = mockMvc.perform(
            get(
                "/evaluation?" +
                    "url1=${resourceLoader.getResource("classpath:static/test1.csv").url}&" +
                    "url2=${resourceLoader.getResource("classpath:static/test2.csv").url}"
            )
        )
            .andExpect(status().isOk())
            .andReturn().response.contentAsString
            .let { objectMapper.readValue(it, Response::class.java) }

        assertEquals("Bale Allwright", response.mostSpeeches)
        assertEquals("Carey Goodspeed", response.mostSecurity)
        assertEquals("Garrek Coleford", response.leastWordy)
    }
}