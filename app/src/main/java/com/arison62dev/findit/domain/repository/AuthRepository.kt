package com.arison62dev.findit.domain.repository

import android.util.Log
import com.arison62dev.findit.data.Result
import com.arison62dev.findit.data.local.AuthDataStore
import com.arison62dev.findit.data.model.UtilisateurDto
import com.arison62dev.findit.data.model.tableName
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.arison62dev.findit.util.PasswordHasher.hash


interface AuthRepository {
    suspend fun login(email: String, password: String): Result<UtilisateurDto?>
    suspend fun signUp(fullName: String, email: String, password: String): Result<UtilisateurDto?>
}

class AuthRepositoryImpl @Inject constructor(
    private val supabaseAuth: Auth,
    private val postgrest: Postgrest,
    private val authDataStore: AuthDataStore,
) : AuthRepository {
    override suspend fun login(emailArg: String, passwordArg: String): Result<UtilisateurDto?> {

        return withContext(Dispatchers.IO) {
            try {
                // check if user have an account
                val userFound = postgrest.from(UtilisateurDto.tableName).select {
                    filter {
                        eq("email", emailArg)
                    }
                }.decodeSingleOrNull<UtilisateurDto>()



                if (userFound != null) {
                    supabaseAuth.signInWith(Email) {
                        email = emailArg
                        password = passwordArg
                    }
                    authDataStore.setLoggedIn(true)
                    return@withContext Result.Success(userFound)
                } else {
                    return@withContext Result.Error(Exception("User not found"))
                }

            } catch (e: java.lang.Exception) {
                Log.d("AuthRepository@login", "$e")
                return@withContext Result.Error(e)
            }

        }
    }

    override suspend fun signUp(
        fullName: String, emailArg: String, passwordArg: String
    ): Result<UtilisateurDto?> {
        return withContext(Dispatchers.IO) {
            try {
                val userFound = postgrest.from(UtilisateurDto.tableName).select {
                    filter {
                        eq("email", emailArg)
                    }
                }.decodeSingleOrNull<UtilisateurDto>()
                if (userFound != null) {
                    return@withContext Result.Error(Exception("User already exists"))
                } else {
                    val newUser = UtilisateurDto(
                        nom = fullName,
                        email = emailArg,
                        motDePasse = hash(passwordArg)
                    )

                    supabaseAuth.signUpWith(Email) {
                        password = passwordArg
                        email = emailArg
                    }

                    val insertedUser = postgrest.from(UtilisateurDto.tableName).insert(newUser){
                        select()
                    }.decodeSingleOrNull<UtilisateurDto>()
                    authDataStore.setLoggedIn(true)
                    Log.d("AuthRepository@signUp", "data: $insertedUser")
                    return@withContext Result.Success(insertedUser)
                }

            } catch (e : java.lang.Exception) {
                Log.d("AuthRepository@signUp", "$e")
                return@withContext Result.Error(e)
            }
        }

    }

}