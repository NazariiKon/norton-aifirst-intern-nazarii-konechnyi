package com.example.scammessagedetector.data.analyzer

import com.example.scammessagedetector.data.remote.api.GroqApiService
import com.example.scammessagedetector.data.remote.dto.ChatCompletionRequest
import com.example.scammessagedetector.domain.analyzer.ScamAnalyzer
import com.example.scammessagedetector.domain.model.Result
import com.example.scammessagedetector.domain.model.ScamAnalysisResult
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

// Implementation of ScamAnalyzer using Groq LLM API
// Sends messages to Groq for AI-powered scam analysis
class ApiScamAnalyzer(
    private val groqApiService: GroqApiService,
    private val apiKey: String,
    private val modelName: String
) : ScamAnalyzer {
    
    // System prompt that defines the analyzer's behavior
    // Instructs the LLM to respond with JSON containing risk assessment
    private val systemPrompt = """
        You are a scam detection expert. Analyze the provided message for scam indicators.
        
        Return a JSON object with this exact structure:
        {
            "riskLevel": "SAFE" or "SUSPICIOUS" or "DANGEROUS",
            "confidenceScore": 0.0-1.0,
            "explanation": "Brief reason for this assessment"
        }
        
        Guidelines:
        - SAFE: Legitimate message with no red flags
        - SUSPICIOUS: Some warning signs (urgency, unusual requests)
        - DANGEROUS: Multiple scam indicators (phishing, fake authority, malicious links)
    """.trimIndent()
    
    override suspend fun analyze(message: String): Result<ScamAnalysisResult> {
        return try {
            // Build the chat completion request
            val request = ChatCompletionRequest(
                model = modelName,
                messages = listOf(
                    ChatCompletionRequest.Message(
                        role = "system",
                        content = systemPrompt
                    ),
                    ChatCompletionRequest.Message(
                        role = "user",
                        content = "Analyze this message for scam indicators:\n\n$message"
                    )
                ),
                temperature = 0.3f, // Low temperature for consistent results
                maxTokens = 300
            )
            
            // Call Groq API
            val response = groqApiService.chatCompletion(
                authHeader = "Bearer $apiKey",
                request = request
            )
            
            // Extract the assistant's response
            val assistantMessage = response.choices.firstOrNull()?.message?.content
                ?: return Result.Error(Exception("No response from Groq API"))
            
            // Parse the JSON response from the LLM
            val analysisResult = parseAnalysisResponse(assistantMessage, message)
            Result.Success(analysisResult)
            
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    // Parses the LLM's JSON response into a ScamAnalysisResult
    // Handles malformed JSON gracefully with fallback values
    private fun parseAnalysisResponse(
        jsonResponse: String,
        originalMessage: String
    ): ScamAnalysisResult {
        return try {
            val json = Json.parseToJsonElement(jsonResponse).jsonObject
            
            ScamAnalysisResult(
                riskLevel = json["riskLevel"]?.jsonPrimitive?.content ?: "SUSPICIOUS",
                confidenceScore = (json["confidenceScore"]?.jsonPrimitive?.content?.toFloatOrNull() ?: 0.5f)
                    .coerceIn(0f, 1f), // Ensure score is between 0.0 and 1.0
                explanation = json["explanation"]?.jsonPrimitive?.content ?: "Unable to analyze message",
                analyzedMessage = originalMessage
            )
        } catch (e: Exception) {
            // Fallback response if JSON parsing fails
            ScamAnalysisResult(
                riskLevel = "SUSPICIOUS",
                confidenceScore = 0.5f,
                explanation = "Analysis failed: ${e.message}",
                analyzedMessage = originalMessage
            )
        }
    }
}
