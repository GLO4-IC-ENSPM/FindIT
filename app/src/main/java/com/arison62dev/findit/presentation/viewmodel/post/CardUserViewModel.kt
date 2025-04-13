package com.arison62dev.findit.presentation.viewmodel.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arison62dev.findit.data.Result
import com.arison62dev.findit.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UserState(
    val email: String? = null,
    val name: String? = null,
    val profilePictureUrl: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CardUserViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    // Utiliser StateFlow au lieu de mutableStateOf pour une meilleure gestion des états
    private val _userStateMap = MutableStateFlow<Map<Int, UserState>>(emptyMap())
    val userStateMap: StateFlow<Map<Int, UserState>> = _userStateMap.asStateFlow()

    // Cache des utilisateurs déjà chargés
    private val userCache = mutableMapOf<Int, UserState>()

    fun getUserById(id: Int) {
        // Vérifier si l'utilisateur est déjà dans le cache
        userCache[id]?.let { cachedState ->
            // Ne pas recharger si déjà en cache et complet
            if (cachedState.email != null && cachedState.name != null) {
                updateUserState(id, cachedState)
                return
            }
        }

        // Si non trouvé dans le cache, ou incomplet, commencer le chargement
        updateUserState(id, UserState(isLoading = true))

        viewModelScope.launch {
            try {
                when(val result = authRepository.getUserById(id)) {
                    is Result.Success -> {
                        val newState = UserState(
                            email = result.data.email,
                            name = result.data.nom,
                            profilePictureUrl = null,
                            isLoading = false
                        )

                        // Mettre à jour le cache et l'état
                        userCache[id] = newState
                        updateUserState(id, newState)
                    }
                    is Result.Error -> {
                        val errorState = UserState(
                            isLoading = false,
                            error = result.exception.message ?: "Une erreur est survenue"
                        )
                        updateUserState(id, errorState)
                    }
                }
            } catch (e: Exception) {
                val errorState = UserState(
                    isLoading = false,
                    error = e.message ?: "Une erreur est survenue"
                )
                updateUserState(id, errorState)
            }
        }
    }

    private fun updateUserState(id: Int, state: UserState) {
        val currentMap = _userStateMap.value.toMutableMap()
        currentMap[id] = state
        _userStateMap.value = currentMap
    }

    // Méthode d'aide pour obtenir l'état d'un utilisateur spécifique
    fun getUserState(id: Int): UserState {
        return _userStateMap.value[id] ?: UserState(isLoading = true).also {
            // Déclencher automatiquement le chargement si non trouvé
            getUserById(id)
        }
    }

    // Méthode pour effacer le cache si nécessaire
    fun clearCache() {
        userCache.clear()
        _userStateMap.value = emptyMap()
    }
}