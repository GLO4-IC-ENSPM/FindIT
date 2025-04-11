package com.arison62dev.findit.presentation.navigation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class NavigationManager @Inject constructor() : ViewModel() {
    private val _navigationEvents = MutableSharedFlow<String>()
    val navigationEvents = _navigationEvents.asSharedFlow()
    private lateinit var navController: NavHostController

    fun setNavController(navController: NavHostController) {
        this.navController = navController
    }

    fun navigateTo(route: String){
        viewModelScope.launch {
            _navigationEvents.emit(route)
        }
    }
}