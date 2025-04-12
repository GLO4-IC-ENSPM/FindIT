package com.arison62dev.findit.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arison62dev.findit.data.Result
import com.arison62dev.findit.data.local.AuthDataStore
import com.arison62dev.findit.data.mapper.toDomain
import com.arison62dev.findit.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val authDataStore: AuthDataStore
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                when (val result = authRepository.login(email, password)) {
                    is Result.Success -> {
                        val user = result.data
                        if (user != null) {
                            authDataStore.setLoggedIn(true)
                            user.idUtilisateur?.let { authDataStore.setUserId(it) }
                        } else {
                            _loginState.value = LoginState.Error("Authentification echoue")
                        }
                    }

                    is Result.Error -> _loginState.value =
                        LoginState.Error(result.exception.message ?: "Erreur de connexion")
                }
            } catch (e: Exception) {
                Log.d("LoginViewModel", "login: ${e.message}")
                _loginState.value = LoginState.Error(e.message ?: "Erreur de connexion")
            }
            Log.d("LoginViewModel", "login: ${_loginState.value}")
        }
    }


}

