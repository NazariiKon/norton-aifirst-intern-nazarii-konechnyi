package com.example.scammessagedetector.domain.usecase

import com.example.scammessagedetector.domain.model.Result
import com.example.scammessagedetector.domain.model.RiskLevel
import com.example.scammessagedetector.domain.model.ScamAnalysisResult
import com.example.scammessagedetector.domain.repository.ScamAnalyzerRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class AnalyzeScamMessageUseCaseTest {

    @Mock
    private lateinit var repository: ScamAnalyzerRepository

    private lateinit var analyzeScamMessageUseCase: AnalyzeScamMessageUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        analyzeScamMessageUseCase = AnalyzeScamMessageUseCase(repository)
    }

    @Test
    fun `execute returns success when repository returns success`() = runTest {
        // Given
        val message = "Test message"
        val expectedResult = ScamAnalysisResult(
            riskLevel = RiskLevel.SAFE.name,
            confidenceScore = 0.95f,
            explanation = "Looks safe",
            analyzedMessage = message
        )
        whenever(repository.analyzeMessage(message)).thenReturn(Result.Success(expectedResult))

        // When
        val result = analyzeScamMessageUseCase.execute(message)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(expectedResult, (result as Result.Success).data)
    }

    @Test
    fun `execute trims message before calling repository`() = runTest {
        // Given
        val message = "  Test message  "
        val trimmedMessage = "Test message"
        val expectedResult = ScamAnalysisResult(
            riskLevel = RiskLevel.SAFE.name,
            confidenceScore = 0.95f,
            explanation = "Looks safe",
            analyzedMessage = trimmedMessage
        )
        whenever(repository.analyzeMessage(trimmedMessage)).thenReturn(Result.Success(expectedResult))

        // When
        val result = analyzeScamMessageUseCase.execute(message)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(expectedResult, (result as Result.Success).data)
    }

    @Test
    fun `execute returns error when repository returns error`() = runTest {
        // Given
        val message = "Test message"
        val exception = Exception("API Error")
        whenever(repository.analyzeMessage(message)).thenReturn(Result.Error(exception))

        // When
        val result = analyzeScamMessageUseCase.execute(message)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)
    }
}
