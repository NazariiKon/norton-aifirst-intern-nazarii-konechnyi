package com.example.scammessagedetector.presentation

import com.example.scammessagedetector.domain.model.Result
import com.example.scammessagedetector.domain.model.ScamAnalysisResult
import com.example.scammessagedetector.domain.repository.ScamAnalyzerRepository
import com.example.scammessagedetector.domain.usecase.AnalyzeScamMessageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * AI-GENERATED: This test class was generated with AI assistance to cover ViewModel state transitions
 * and interaction with use cases.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ScamDetectorViewModelTest {

    @Mock
    private lateinit var analyzeUseCase: AnalyzeScamMessageUseCase
    
    @Mock
    private lateinit var repository: ScamAnalyzerRepository

    private lateinit var viewModel: ScamDetectorViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        runBlocking {
            whenever(repository.getExampleScams()).thenReturn(Result.Success(emptyList()))
        }
        viewModel = ScamDetectorViewModel(analyzeUseCase, repository)
        testDispatcher.scheduler.advanceUntilIdle()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // AI-GENERATED: Ensures analysis isn't triggered for empty inputs (UI validation)
    @Test
    fun `analyzeMessage() does nothing if message is empty`() = runTest {
        viewModel.onMessageChange("")
        viewModel.analyzeMessage()

        testDispatcher.scheduler.advanceUntilIdle()

        verify(analyzeUseCase, never()).execute(any())
    }

    // AI-GENERATED: Verifies successful state update after analysis
    @Test
    fun `analyzeMessage() sets analysisResult on success`() = runTest {
        val message = "test"
        val expected = ScamAnalysisResult("SAFE", 1f, "ok", message)
        whenever(analyzeUseCase.execute(message)).thenReturn(Result.Success(expected))

        viewModel.onMessageChange(message)
        viewModel.analyzeMessage()
        
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(expected, viewModel.uiState.value.analysisResult)
        assertNull(viewModel.uiState.value.error)
    }

    // AI-GENERATED: Verifies error handling in the UI state
    @Test
    fun `analyzeMessage() sets error on error`() = runTest {
        val message = "test"
        val errorMsg = "API Error"
        whenever(analyzeUseCase.execute(message)).thenReturn(Result.Error(Exception(errorMsg)))

        viewModel.onMessageChange(message)
        viewModel.analyzeMessage()
        
        testDispatcher.scheduler.advanceUntilIdle()

        assertNull(viewModel.uiState.value.analysisResult)
        assertEquals(errorMsg, viewModel.uiState.value.error)
    }

    // AI-GENERATED: Verifies UX logic of clearing results when new input is provided
    @Test
    fun `onMessageChange() clears previous analysisResult`() = runTest {
        val message = "test"
        val res = ScamAnalysisResult("SAFE", 1f, "ok", message)
        whenever(analyzeUseCase.execute(message)).thenReturn(Result.Success(res))

        viewModel.onMessageChange(message)
        viewModel.analyzeMessage()
        testDispatcher.scheduler.advanceUntilIdle()
        
        assertEquals(res, viewModel.uiState.value.analysisResult)

        viewModel.onMessageChange("new message")
        
        assertNull(viewModel.uiState.value.analysisResult)
    }
}
