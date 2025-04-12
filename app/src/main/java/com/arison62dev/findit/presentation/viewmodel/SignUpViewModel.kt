package com.arison62dev.findit.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arison62dev.findit.data.Result
import com.arison62dev.findit.data.local.AuthDataStore
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
    private val authRepository: AuthRepository,
    private val authDataStore: AuthDataStore,
) : ViewModel() {

    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val signUpState: StateFlow<SignUpState> = _signUpState

    fun signUp(fullName: String, email: String, password: String) {
        viewModelScope.launch {
            _signUpState.value = SignUpState.Loading
            try {
                when(val result =  authRepository.signUp(fullName, email, password)){
                    is Result.Success ->{
                        val user = result.data
                        if(user == null){
                            _signUpState.value = SignUpState.Error("Erreur lors de l'inscription")

                        }else{
                             authDataStore.setLoggedIn(true)
                            user.idUtilisateur?.let { authDataStore.setUserId(it) }
                            _signUpState.value = SignUpState.Success

                        }

                    }
                    is Result.Error ->{
                        _signUpState.value = SignUpState.Error(result.exception.message ?: "Erreur lors de l'inscription")
                    }
                }
            } catch (e: Exception) {
                _signUpState.value = SignUpState.Error(e.message ?: "Erreur lors de l'inscription")
            }
        }
    }
}