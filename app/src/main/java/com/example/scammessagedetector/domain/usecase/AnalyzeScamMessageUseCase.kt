package com.example.scammessagedetector.domain.usecase

import com.example.scammessagedetector.domain.model.Result
import com.example.scammessagedetector.domain.model.ScamAnalysisResult
import com.example.scammessagedetector.domain.repository.ScamAnalyzerRepository

/**
 * Use case for analyzing a message for scam indicators.
 * Contains validation logic to ensure message is not empty and fits size limits.
 */
class AnalyzeScamMessageUseCase(
    private val repository: ScamAnalyzerRepository
) {
    /**
     * Executes the analysis after validating business rules.
     */
    suspend fun execute(message: String): Result<ScamAnalysisResult> {
        // Validation logic belongs here in the UseCase layer
        if (message.isBlank()) {
            return Result.Error(Exception("Message cannot be empty"))
        }
        
        if (message.length > 2000) {
            return Result.Error(Exception("Message is too long (max 2000 chars)"))
        }

        return repository.analyzeMessage(message)
    }
}
