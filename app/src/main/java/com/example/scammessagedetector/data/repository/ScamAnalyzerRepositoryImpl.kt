package com.example.scammessagedetector.data.repository

import com.example.scammessagedetector.domain.analyzer.ScamAnalyzer
import com.example.scammessagedetector.domain.model.ExampleScam
import com.example.scammessagedetector.domain.model.Result
import com.example.scammessagedetector.domain.model.ScamAnalysisResult
import com.example.scammessagedetector.domain.repository.ScamAnalyzerRepository

/**
 * Implementation of ScamAnalyzerRepository.
 * Delegates analysis to the ScamAnalyzer and provides example scams.
 */
class ScamAnalyzerRepositoryImpl(
    private val scamAnalyzer: ScamAnalyzer
) : ScamAnalyzerRepository {
    
    override suspend fun analyzeMessage(message: String): Result<ScamAnalysisResult> {
        // Validation is handled in the UseCase layer
        // This layer focus on delegating to the analyzer (could be API-based, heuristic, etc.)
        return scamAnalyzer.analyze(message)
    }
    
    override suspend fun getExampleScams(): Result<List<ExampleScam>> {
        return try {
            Result.Success(providedExamples)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    companion object {
        /**
         * Hardcoded example scam messages for demonstration.
         */
        private val providedExamples = listOf(
            ExampleScam(
                title = "Phishing Email",
                message = "URGENT: Your PayPal account has been locked. Verify your credentials immediately at paypal-verify-security.ru to restore access.",
                expectedRiskLevel = "DANGEROUS"
            ),
            ExampleScam(
                title = "Tech Support Scam",
                message = "⚠️ ALERT: Microsoft Support detected a virus on your computer! Call 1-800-MICROSOFT immediately to fix the problem. Do not ignore this message!",
                expectedRiskLevel = "DANGEROUS"
            ),
            ExampleScam(
                title = "Prize Scam",
                message = "Congratulations! You've won $1,000,000 in our lottery! Click here to claim: win-big-money.xyz. Your lucky number is 7734.",
                expectedRiskLevel = "DANGEROUS"
            ),
            ExampleScam(
                title = "Bank Fraud Alert",
                message = "Your bank account has suspicious activity. Update your banking details at secure-update-bank.com within 24 hours or your account will be closed.",
                expectedRiskLevel = "DANGEROUS"
            ),
            ExampleScam(
                title = "Package Delivery Scam",
                message = "Your package delivery failed. Click here to reschedule: delivery-tracking-xyz.net or call our hotline.",
                expectedRiskLevel = "SUSPICIOUS"
            ),
            ExampleScam(
                title = "Legitimate Message",
                message = "Hi John, this is your dentist's office reminding you of your appointment tomorrow at 2 PM. Please reply to confirm.",
                expectedRiskLevel = "SAFE"
            )
        )
    }
}
