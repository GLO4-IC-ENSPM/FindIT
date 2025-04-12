package com.arison62dev.findit.di

import com.arison62dev.findit.data.local.AuthDataStore
import com.arison62dev.findit.domain.repository.PostRepository
import com.arison62dev.findit.domain.repository.PostRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PostModule {

    @Provides
    @Singleton
    fun providePostRepository(
        storage: Storage,
        postgrest: Postgrest,
        authDataStore: AuthDataStore
    ): PostRepository {
        return PostRepositoryImpl(
            storage = storage,
            postgrest = postgrest,
            authDataStore = authDataStore
        )
    }
}