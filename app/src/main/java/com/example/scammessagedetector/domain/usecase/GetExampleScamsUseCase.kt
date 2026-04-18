package com.example.scammessagedetector.domain.usecase

import com.example.scammessagedetector.domain.model.ExampleScam
import com.example.scammessagedetector.domain.model.Result
import com.example.scammessagedetector.domain.repository.ScamAnalyzerRepository

// Use case for retrieving example scam messages
// Provides sample scams for demonstration and testing
class GetExampleScamsUseCase(
    private val repository: ScamAnalyzerRepository
) {
    // Executes to fetch all available example scams
    // Returns: Result containing list of ExampleScam or an exception
    suspend fun execute(): Result<List<ExampleScam>> {
        return repository.getExampleScams()
    }
}
