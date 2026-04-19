package com.example.scammessagedetector

import com.example.scammessagedetector.data.analyzer.ApiScamAnalyzer
import com.example.scammessagedetector.data.remote.GroqRetrofitClient
import com.example.scammessagedetector.data.repository.ScamAnalyzerRepositoryImpl
import com.example.scammessagedetector.domain.model.Result
import com.example.scammessagedetector.domain.usecase.AnalyzeScamMessageUseCase
import com.example.scammessagedetector.utils.Constants
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Integration test that makes actual API calls to Groq.
 */
class ApiIntegrationTest {

    private lateinit var analyzeUseCase: AnalyzeScamMessageUseCase
    private lateinit var repository: ScamAnalyzerRepositoryImpl

    @Before
    fun setUp() {
        val groqApiService = GroqRetrofitClient.createGroqApiService()
        
        // Note: For actual integration tests, you can use BuildConfig.GROQ_API_KEY if it's available in test source set,
        // or a hardcoded test key if preferred. Here we switch to BuildConfig to match AppModule.
        val analyzer = ApiScamAnalyzer(
            groqApiService = groqApiService,
            apiKey = BuildConfig.GROQ_API_KEY,
            modelName = Constants.GROQ_MODEL
        )
        repository = ScamAnalyzerRepositoryImpl(analyzer)
        analyzeUseCase = AnalyzeScamMessageUseCase(repository)
    }

    @Test
    fun `test actual API scam analysis`() = runTest {
        // Given
        val testMessage = "Click here to verify your PayPal: paypal-verify.ru"

        // When
        val result = analyzeUseCase.execute(testMessage)

        // Then
        println("API Result: $result")
        assertTrue("Expected Success but got $result", result is Result.Success)
        val analysis = (result as Result.Success).data
        assertNotNull(analysis.riskLevel)
        assertNotNull(analysis.explanation)
        println("Analysis: $analysis")
    }

    @Test
    fun `test actual examples retrieval`() = runTest {
        // When
        val result = repository.getExampleScams()

        // Then
        assertTrue("Expected Success but got $result", result is Result.Success)
        val examples = (result as Result.Success).data
        assertTrue("Examples list should not be empty", examples.isNotEmpty())
    }
}
