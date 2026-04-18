package com.example.scammessagedetector.domain.repository

import com.example.scammessagedetector.domain.model.ExampleScam
import com.example.scammessagedetector.domain.model.Result
import com.example.scammessagedetector.domain.model.ScamAnalysisResult

// Interface for the scam analyzer repository
// This defines the contract for analyzing messages and retrieving example scams
interface ScamAnalyzerRepository {
    // Analyzes a message using the LLM API
    // Returns Result.Success with ScamAnalysisResult or Result.Error on failure
    suspend fun analyzeMessage(message: String): Result<ScamAnalysisResult>
    
    // Retrieves a list of example scam messages for demonstration
    suspend fun getExampleScams(): Result<List<ExampleScam>>
}
