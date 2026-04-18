package com.example.scammessagedetector.domain.model

// Sealed class for handling success and failure scenarios in use cases
// This allows us to return either Success with data or Error with an exception
sealed class Result<out T> {
    // Represents a successful operation with data
    data class Success<T>(val data: T) : Result<T>()
    
    // Represents a failed operation with an error message
    data class Error(val exception: Exception) : Result<Nothing>()
    
    // Represents a loading state (optional, for UI feedback)
    data object Loading : Result<Nothing>()
}
