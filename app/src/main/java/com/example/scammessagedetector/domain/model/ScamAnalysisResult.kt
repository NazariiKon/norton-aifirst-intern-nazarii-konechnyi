package com.example.scammessagedetector.domain.model

import kotlinx.serialization.Serializable

// Data class representing the result of analyzing a message for scam content
// riskLevel: The assessed risk level (Safe, Suspicious, or Dangerous)
// confidenceScore: A value between 0.0 and 1.0 indicating analysis confidence (1.0 = 100% confident)
// explanation: A human-readable explanation of why this risk level was assigned
// analyzedMessage: The original message that was analyzed
@Serializable
data class ScamAnalysisResult(
    val riskLevel: String, // "SAFE", "SUSPICIOUS", or "DANGEROUS"
    val confidenceScore: Float, // Range: 0.0 - 1.0
    val explanation: String,
    val analyzedMessage: String
)