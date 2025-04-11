package com.arison62dev.findit.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arison62dev.findit.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SignUpState {
    object Idle : SignUpState()
    object Loading : SignUpState()
    object Success : SignUpState()
    data class Error(val message: String) : SignUpState()
}

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val signUpState: StateFlow<SignUpState> = _signUpState

    fun signUp(fullName: String, email: String, password: String) {
        viewModelScope.launch {
            _signUpState.value = SignUpState.Loading
            try {
                authRepository.signUp(fullName, email, password)
                _signUpState.value = SignUpState.Success
            } catch (e: Exception) {
                _signUpState.value = SignUpState.Error(e.message ?: "Erreur lors de l'inscription")
            }
        }
    }
}