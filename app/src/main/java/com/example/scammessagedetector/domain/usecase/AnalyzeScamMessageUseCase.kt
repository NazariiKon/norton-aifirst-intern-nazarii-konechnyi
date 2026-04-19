package com.example.scammessagedetector.domain.usecase

import com.example.scammessagedetector.domain.model.Result
import com.example.scammessagedetector.domain.model.ScamAnalysisResult
import com.example.scammessagedetector.domain.repository.ScamAnalyzerRepository

class AnalyzeScamMessageUseCase(
    private val repository: ScamAnalyzerRepository
) {
    suspend fun execute(message: String): Result<ScamAnalysisResult> {
        return repository.analyzeMessage(message)
    }
}
