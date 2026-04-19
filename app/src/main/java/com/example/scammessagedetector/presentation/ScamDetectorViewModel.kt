package com.example.scammessagedetector.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scammessagedetector.domain.model.ExampleScam
import com.example.scammessagedetector.domain.model.Result
import com.example.scammessagedetector.domain.model.ScamAnalysisResult
import com.example.scammessagedetector.domain.usecase.AnalyzeScamMessageUseCase
import com.example.scammessagedetector.domain.repository.ScamAnalyzerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScamDetectorViewModel @Inject constructor(
    private val analyzeScamMessageUseCase: AnalyzeScamMessageUseCase,
    private val repository: ScamAnalyzerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScamDetectorUiState())
    val uiState: StateFlow<ScamDetectorUiState> = _uiState.asStateFlow()

    init {
        loadExamples()
    }

    fun onMessageChange(newMessage: String) {
        // Clear result when user types new message
        _uiState.update { it.copy(messageInput = newMessage, analysisResult = null, error = null) }
    }

    fun analyzeMessage() {
        val message = _uiState.value.messageInput
        
        // Basic validation for empty input
        if (message.isBlank()) return

        // Limit message length to avoid LLM context issues (simple validation)
        if (message.length > 2000) {
            _uiState.update { it.copy(error = "Message is too long (max 2000 chars)") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = analyzeScamMessageUseCase.execute(message)) {
                is Result.Success -> {
                    _uiState.update { it.copy(analysisResult = result.data, isLoading = false) }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(error = result.exception.message, isLoading = false) }
                }
                is Result.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun loadExamples() {
        viewModelScope.launch {
            when (val result = repository.getExampleScams()) {
                is Result.Success -> {
                    _uiState.update { it.copy(examples = result.data) }
                }
                is Result.Error -> {
                    // Handle error if needed
                }
                is Result.Loading -> {
                    // Handle loading if needed
                }
            }
        }
    }
}

data class ScamDetectorUiState(
    val messageInput: String = "",
    val isLoading: Boolean = false,
    val analysisResult: ScamAnalysisResult? = null,
    val examples: List<ExampleScam> = emptyList(),
    val error: String? = null
)
