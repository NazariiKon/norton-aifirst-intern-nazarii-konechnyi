package com.example.scammessagedetector.data.analyzer

import com.example.scammessagedetector.data.remote.api.GroqApiService
import com.example.scammessagedetector.data.remote.dto.ChatCompletionResponse
import com.example.scammessagedetector.domain.model.Result
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

/**
 * AI-GENERATED: This test class was created with AI assistance to validate JSON parsing and error handling logic.
 */
class ApiScamAnalyzerTest {

    @Mock
    private lateinit var apiService: GroqApiService

    private lateinit var analyzer: ApiScamAnalyzer

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        analyzer = ApiScamAnalyzer(apiService, "fake_key", "fake_model")
    }

    // AI-GENERATED: Validates mapping logic from complex JSON response to domain model
    @Test
    fun `correctly maps API response to ScamAnalysisResult`() = runTest {
        val mockJson = """
            {
                "riskLevel": "DANGEROUS",
                "confidenceScore": 0.95,
                "explanation": "Phishing link detected"
            }
        """.trimIndent()
        
        val mockResponse = createMockResponse(mockJson)
        whenever(apiService.chatCompletion(any(), any())).thenReturn(mockResponse)

        val result = analyzer.analyze("some message")

        assertTrue(result is Result.Success)
        val data = (result as Result.Success).data
        assertEquals("DANGEROUS", data.riskLevel)
        assertEquals(0.95f, data.confidenceScore)
        assertEquals("Phishing link detected", data.explanation)
    }

    // AI-GENERATED: Verifies the analyzer's resilience against network failures
    @Test
    fun `returns Result Error on network error`() = runTest {
        whenever(apiService.chatCompletion(any(), any())).thenThrow(RuntimeException("No internet"))

        val result = analyzer.analyze("some message")

        assertTrue(result is Result.Error)
        assertEquals("No internet", (result as Result.Error).exception.message)
    }

    private fun createMockResponse(content: String) = ChatCompletionResponse(
        id = "1",
        objectType = "chat.completion",
        created = 123456789L,
        model = "model",
        usage = ChatCompletionResponse.Usage(1, 1, 2),
        choices = listOf(
            ChatCompletionResponse.Choice(
                index = 0,
                message = ChatCompletionResponse.Choice.Message(role = "assistant", content = content),
                finishReason = "stop"
            )
        )
    )
}
