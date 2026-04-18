package com.example.scammessagedetector.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Request DTO for Groq/OpenAI Chat Completions API
@Serializable
data class ChatCompletionRequest(
    val model: String,
    val messages: List<Message>,
    @SerialName("temperature")
    val temperature: Float = 0.7f,
    @SerialName("max_tokens")
    val maxTokens: Int = 500
) {
    // Represents a single message in the conversation
    @Serializable
    data class Message(
        val role: String, // "system", "user", or "assistant"
        val content: String
    )
}

// Response DTO for Groq/OpenAI Chat Completions API
@Serializable
data class ChatCompletionResponse(
    val id: String,
    @SerialName("object")
    val objectType: String,
    val created: Long,
    val model: String,
    val choices: List<Choice>,
    val usage: Usage
) {
    @Serializable
    data class Choice(
        val index: Int,
        val message: Message,
        @SerialName("finish_reason")
        val finishReason: String
    ) {
        @Serializable
        data class Message(
            val role: String,
            val content: String
        )
    }
    
    @Serializable
    data class Usage(
        @SerialName("prompt_tokens")
        val promptTokens: Int,
        @SerialName("completion_tokens")
        val completionTokens: Int,
        @SerialName("total_tokens")
        val totalTokens: Int
    )
}
