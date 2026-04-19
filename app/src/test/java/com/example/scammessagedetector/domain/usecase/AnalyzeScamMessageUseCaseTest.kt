package com.example.scammessagedetector.domain.usecase

import com.example.scammessagedetector.domain.model.Result
import com.example.scammessagedetector.domain.model.ScamAnalysisResult
import com.example.scammessagedetector.domain.repository.ScamAnalyzerRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * AI-GENERATED: This test class was generated using AI to ensure coverage of UseCase business logic.
 */
class AnalyzeScamMessageUseCaseTest {

    @Mock
    private lateinit var repository: ScamAnalyzerRepository

    private lateinit var useCase: AnalyzeScamMessageUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        useCase = AnalyzeScamMessageUseCase(repository)
    }

    // AI-GENERATED: Verifies successful data propagation
    @Test
    fun `execute returns success when repository returns result`() = runTest {
        val message = "suspicious text"
        val expected = ScamAnalysisResult("DANGEROUS", 0.9f, "Scam", message)
        whenever(repository.analyzeMessage(message)).thenReturn(Result.Success(expected))

        val result = useCase.execute(message)

        assertTrue(result is Result.Success)
        assertEquals(expected, (result as Result.Success).data)
    }

    // AI-GENERATED: Verifies error handling for repository exceptions
    @Test
    fun `execute returns error when repository throws exception`() = runTest {
        val message = "text"
        val exception = RuntimeException("Network error")
        whenever(repository.analyzeMessage(message)).thenReturn(Result.Error(exception))

        val result = useCase.execute(message)

        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)
    }

    // AI-GENERATED: Ensures the message is passed to the data layer without modification
    @Test
    fun `execute passes message to repository without changes`() = runTest {
        val message = "Original Message"
        whenever(repository.analyzeMessage(message)).thenReturn(Result.Error(Exception()))

        useCase.execute(message)

        verify(repository).analyzeMessage(message)
    }
}
