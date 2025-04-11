package com.arison62dev.findit.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arison62dev.findit.data.local.AuthDataStore
import com.arison62dev.findit.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
) : ViewModel() {
    private val _startDestination = MutableStateFlow<String>(Screen.LoginScreen.route)
    val startDestination = _startDestination

    init {
        Log.d("SplashViewModel", "SplashViewModel initialized")
        checkUserLoggedIn()
    }

    private fun checkUserLoggedIn() {
        viewModelScope.launch {
            val loggedIn = authDataStore.isLoggedIn.first()
            if (loggedIn) {
                _startDestination.value = Screen.HomeScreen.route
            } else {
                _startDestination.value = Screen.LoginScreen.route
            }
        }
    }
}