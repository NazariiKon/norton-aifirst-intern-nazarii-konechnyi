package com.example.scammessagedetector.domain.usecase

import com.example.scammessagedetector.domain.model.Result
import com.example.scammessagedetector.domain.model.ScamAnalysisResult
import com.example.scammessagedetector.domain.repository.ScamAnalyzerRepository

// Use case for analyzing a message for scam indicators
// Encapsulates the business logic for message analysis
class AnalyzeScamMessageUseCase(
    private val repository: ScamAnalyzerRepository
) {
    // Executes the message analysis
    // message: The text to analyze (SMS, email, URL, etc.)
    // Returns: Result containing ScamAnalysisResult or an exception
    suspend fun execute(message: String): Result<ScamAnalysisResult> {
        // Trim whitespace
        val trimmedMessage = message.trim()
        
        // Delegate to repository
        return repository.analyzeMessage(trimmedMessage)
    }
}