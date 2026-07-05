package com.example.contexto.ai

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import android.util.Log
import com.example.contexto.BuildConfig

object OpenRouterClient {

    private const val BASE_URL = "https://openrouter.ai/api/v1/chat/completions"
    private val API_KEY = BuildConfig.OPENROUTER_API_KEY

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            })
        }
    }

    suspend fun analyzeText(title: String, body: String): String {
        val systemPrompt = "You are a mobile AI context engineer. Analyze this notification context. If it requires urgent human action, output a 1-sentence tasks summary. If not, output exact text: IGNORE."
        val userPrompt = "Notification App Data -> Title: $title | Body: $body"

        try {
            val response: OpenRouterResponse = client.post(BASE_URL) {
                header("Authorization", "Bearer $API_KEY")
                header("Content-Type", "application/json")
                // OpenRouter optional tracking headers
                header("HTTP-Referer", "https://github.com/avector/withme") 
                header("X-Title", "Contexto Engine")
                
                setBody(
                    OpenRouterRequest(
                        model = "meta-llama/llama-3.1-8b-instruct:free", // Free tier model
                        messages = listOf(
                            ChatMessage(role = "system", content = systemPrompt),
                            ChatMessage(role = "user", content = userPrompt)
                        )
                    )
                )
            }.body()

            return response.choices.firstOrNull()?.message?.content?.trim() ?: "IGNORE"
        } catch (e: Exception) {
            Log.e("OpenRouterClient", "API execution error", e)
            return "IGNORE"
        }
    }
}
