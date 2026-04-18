package com.example.scammessagedetector.data.remote.api

import com.example.scammessagedetector.data.remote.dto.ChatCompletionRequest
import com.example.scammessagedetector.data.remote.dto.ChatCompletionResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

// Retrofit API interface for Groq/OpenAI Chat Completions endpoint
interface GroqApiService {
    // Calls the chat completions endpoint
    // Returns: ChatCompletionResponse with the LLM's analysis
    @POST("openai/v1/chat/completions")
    suspend fun chatCompletion(
        @Header("Authorization") authHeader: String, // "Bearer {API_KEY}"
        @Body request: ChatCompletionRequest
    ): ChatCompletionResponse
}
