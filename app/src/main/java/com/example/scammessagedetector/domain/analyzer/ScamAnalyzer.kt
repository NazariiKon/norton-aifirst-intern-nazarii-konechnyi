package com.example.scammessagedetector.domain.analyzer

import com.example.scammessagedetector.domain.model.Result
import com.example.scammessagedetector.domain.model.ScamAnalysisResult

// Interface for scam message analysis strategy
// Allows for different implementations (e.g., API-based, heuristic-based, etc.)
interface ScamAnalyzer {
    // Analyzes a message and returns a risk assessment
    // message: The text to analyze (SMS, email, URL, etc.)
    // Returns: Result containing ScamAnalysisResult or an exception
    suspend fun analyze(message: String): Result<ScamAnalysisResult>
}
