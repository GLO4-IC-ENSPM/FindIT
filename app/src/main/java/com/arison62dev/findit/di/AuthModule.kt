package com.arison62dev.findit.di

import com.arison62dev.findit.data.local.AuthDataStore
import com.arison62dev.findit.domain.repository.AuthRepository
import com.arison62dev.findit.domain.repository.AuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        supabaseAuth: Auth,
        postgrest: Postgrest,
        authDataStore: AuthDataStore
    ): AuthRepository {
        return AuthRepositoryImpl(
            supabaseAuth,
            postgrest,
            authDataStore
        )
    }
}