package com.example.scammessagedetector.data.remote

import com.example.scammessagedetector.data.remote.api.GroqApiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

// Factory for creating the Groq API service
class GroqRetrofitClient {
    companion object {
        private const val BASE_URL = "https://api.groq.com/"
        
        // Creates and configures Retrofit with OkHttp client for Groq API
        fun createGroqApiService(): GroqApiService {
            // Configure JSON serialization
            val json = Json {
                ignoreUnknownKeys = true
                prettyPrint = false
            }
            
            // Setup logging interceptor for debugging
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            
            // Create OkHttp client with increased timeouts for AI processing
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS) // AI models may take time to generate response
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
            
            // Create Retrofit instance
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(GroqApiService::class.java)
        }
    }
}
