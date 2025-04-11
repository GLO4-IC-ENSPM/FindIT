package com.arison62dev.findit.domain.repository

import com.arison62dev.findit.data.local.AuthDataStore
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject


interface AuthRepository {
    suspend fun login(email: String, password: String) : Boolean
    suspend fun signUp(fullName: String, email: String, password: String) : Boolean
}

class AuthRepositoryImpl @Inject constructor(
    private val supabaseAuth: Auth,
    private val postgrest: Postgrest,
    private val authDataStore: AuthDataStore,
    private val dispatcherIo : CoroutineDispatcher = Dispatchers.IO
) : AuthRepository {
    override suspend fun login(email: String, password: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun signUp(fullName: String, email: String, password: String): Boolean {
        TODO("Not yet implemented")

    }

}