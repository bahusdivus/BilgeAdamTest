package ru.bahusdivus.bilgeadamtest.services

import org.springframework.stereotype.Service
import ru.bahusdivus.bilgeadamtest.controllers.dto.Response
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class EvaluationService {

    fun getEvaluation(allParams: Map<String, String>) = allParams
        .filter { it.key.matches("^url\\d+$".toRegex()) }
        .map { URL(it.value) }
        .let { doGetEvaluation(it) }


    private fun doGetEvaluation(urls: List<URL>): Response {
        val numSpeechesIn2012: MutableMap<String, Int> = mutableMapOf()
        val numSpeechesAboutSecurity: MutableMap<String, Int> = mutableMapOf()
        val numWordsInSpeeches: MutableMap<String, Int> = mutableMapOf()

        urls.forEach { loadAndAnalyze(it, numSpeechesIn2012, numSpeechesAboutSecurity, numWordsInSpeeches) }

        return Response(
            mostSpeeches = numSpeechesIn2012.maxByOrNull { it.value }?.key,
            mostSecurity = numSpeechesAboutSecurity.maxByOrNull { it.value }?.key,
            leastWordy = numWordsInSpeeches.minByOrNull { it.value }?.key,
        )
    }

    fun loadAndAnalyze(
        url: URL,
        numSpeechesIn2012: MutableMap<String, Int>,
        numSpeechesAboutSecurity: MutableMap<String, Int>,
        numWordsInSpeeches: MutableMap<String, Int>
    ) {
        url.openConnection().getInputStream().bufferedReader().useLines { lines ->
            lines.forEachIndexed { i, line ->
                if (i != 0) processLine(line, numSpeechesIn2012, numSpeechesAboutSecurity, numWordsInSpeeches)
            }
        }
    }

    private fun processLine(
        line: String,
        numSpeechesIn2012: MutableMap<String, Int>,
        numSpeechesAboutSecurity: MutableMap<String, Int>,
        numWordsInSpeeches: MutableMap<String, Int>,
    ) {
        val columns = line.split(";")
        assert(columns.size == 4) { "Wrong number of columns in line: $line" }

        val speaker = columns[0]
        val topic = columns[1]
        val date = LocalDate.parse(columns[2], DateTimeFormatter.ISO_LOCAL_DATE).year
        val numWords = columns[3].toInt()

        if (date == 2012) numSpeechesIn2012[speaker] = (numSpeechesIn2012[speaker] ?: 0) + 1
        if (topic == "homeland security") numSpeechesAboutSecurity[speaker] =
            (numSpeechesAboutSecurity[speaker] ?: 0) + 1
        numWordsInSpeeches[speaker] = (numWordsInSpeeches[speaker] ?: 0) + numWords
    }
}
