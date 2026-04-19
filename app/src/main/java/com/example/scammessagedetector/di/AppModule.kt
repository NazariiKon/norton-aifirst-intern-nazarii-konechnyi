package com.example.scammessagedetector.di

import com.example.scammessagedetector.data.analyzer.ApiScamAnalyzer
import com.example.scammessagedetector.data.remote.GroqRetrofitClient
import com.example.scammessagedetector.data.remote.api.GroqApiService
import com.example.scammessagedetector.data.repository.ScamAnalyzerRepositoryImpl
import com.example.scammessagedetector.domain.analyzer.ScamAnalyzer
import com.example.scammessagedetector.domain.repository.ScamAnalyzerRepository
import com.example.scammessagedetector.domain.usecase.AnalyzeScamMessageUseCase
import com.example.scammessagedetector.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dependency Injection module using Hilt.
 * Provides singleton instances for API services, repositories, and use cases.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provides the Retrofit-based API service for Groq LLM.
     */
    @Provides
    @Singleton
    fun provideGroqApiService(): GroqApiService {
        return GroqRetrofitClient.createGroqApiService()
    }

    /**
     * Provides the ScamAnalyzer implementation that uses the AI API.
     */
    @Provides
    @Singleton
    fun provideScamAnalyzer(groqApiService: GroqApiService): ScamAnalyzer {
        return ApiScamAnalyzer(
            groqApiService = groqApiService,
            apiKey = Constants.GROQ_API_KEY,
            modelName = Constants.GROQ_MODEL
        )
    }

    /**
     * Provides the repository implementation for scam analysis and examples.
     */
    @Provides
    @Singleton
    fun provideScamAnalyzerRepository(scamAnalyzer: ScamAnalyzer): ScamAnalyzerRepository {
        return ScamAnalyzerRepositoryImpl(scamAnalyzer)
    }

    /**
     * Provides the use case for analyzing suspicious messages.
     */
    @Provides
    @Singleton
    fun provideAnalyzeScamMessageUseCase(repository: ScamAnalyzerRepository): AnalyzeScamMessageUseCase {
        return AnalyzeScamMessageUseCase(repository)
    }
}
