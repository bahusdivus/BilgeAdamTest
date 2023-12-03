package ru.bahusdivus.bilgeadamtest.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
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
                    "url2=${resourceLoader.getResource("classpath:static/test2.csv").url}&" +
                    "url3=${resourceLoader.getResource("classpath:static/test3.csv").url}"
            )
        )
            .andExpect(status().isOk())
            .andReturn().response.contentAsString
            .let { objectMapper.readValue(it, Response::class.java) }

        assertEquals("Torey Percifer", response.mostSpeeches)
        assertEquals("Bab Millthorpe", response.mostSecurity)
        assertEquals("Garrek Coleford", response.leastWordy)
    }

    @Test
    fun `should return evaluation with null values`() {
        val response = mockMvc.perform(
            get(
                "/evaluation?" +
                    "url1=${resourceLoader.getResource("classpath:static/test_with_nulls.csv").url}"
            )
        )
            .andExpect(status().isOk())
            .andReturn().response.contentAsString
            .let { objectMapper.readValue(it, Response::class.java) }

        assertNull(response.mostSpeeches)
        assertNull(response.mostSecurity)
        assertEquals("Aluino Gadman", response.leastWordy)
    }

    @Test
    fun `should fail on malformed url and return 400`() {
        mockMvc.perform(get("/evaluation?url1=not-a-url"))
            .andExpect(status().isBadRequest())
    }

    @Test
    fun `should fail on unreachable url and return 400`() {
        mockMvc.perform(get("/evaluation?url1=http://localhost:9999/unexistant_file.csv"))
            .andExpect(status().isBadRequest())
    }

    @Test
    fun `should trow and error on malformed line and return 422`() {
        val url = resourceLoader.getResource("classpath:static/test_malformed_line.csv").url
        mockMvc.perform(get("/evaluation?url1=$url"))
            .andExpect(status().isUnprocessableEntity())
    }

    @Test
    fun `should trow and error on malformed date return 422`() {
        val url = resourceLoader.getResource("classpath:static/test_malformed_date.csv").url
        mockMvc.perform(get("/evaluation?url1=$url"))
            .andExpect(status().isUnprocessableEntity())
    }

    @Test
    fun `should trow and error on malformed words count return 422`() {
        val url = resourceLoader.getResource("classpath:static/test_malformed_words_count.csv").url
        mockMvc.perform(get("/evaluation?url1=$url"))
            .andExpect(status().isUnprocessableEntity())
    }

}