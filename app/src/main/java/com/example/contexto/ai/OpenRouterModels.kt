package com.example.contexto.ai

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val role: String,
    val content: String
)

@Serializable
data class OpenRouterRequest(
    val model: String,
    val messages: List<ChatMessage>,
    val temperature: Float = 0.2f
)

@Serializable
data class OpenRouterResponse(
    val choices: List<Choice>
)

@Serializable
data class Choice(
    val message: ChatMessage
)
