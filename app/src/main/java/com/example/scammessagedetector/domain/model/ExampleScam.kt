package com.example.scammessagedetector.domain.model

import kotlinx.serialization.Serializable

// Data class representing an example scam message for demonstration
// title: A short title describing the type of scam (e.g., "Phishing Email", "Tech Support Scam")
// message: The actual scam message content that users can tap to analyze
// expectedRiskLevel: The expected risk level to validate our analyzer works correctly
@Serializable
data class ExampleScam(
    val title: String,
    val message: String,
    val expectedRiskLevel: String // "SAFE", "SUSPICIOUS", or "DANGEROUS"
)
