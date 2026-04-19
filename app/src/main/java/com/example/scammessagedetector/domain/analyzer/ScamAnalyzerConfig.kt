package com.example.scammessagedetector.domain.analyzer

/**
 * Configuration for the Scam Analyzer.
 * Contains prompts and instructions that define the expert AI behavior.
 * This is kept in the domain layer as it represents business rules for analysis.
 */
object ScamAnalyzerConfig {
    
    /**
     * The system prompt that instructs the AI expert on how to analyze messages.
     */
    val SYSTEM_PROMPT = """
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

    /**
     * Formatting string for the user prompt.
     */
    fun buildUserPrompt(message: String): String {
        return "Analyze this message for scam indicators:\n\n$message"
    }
}
