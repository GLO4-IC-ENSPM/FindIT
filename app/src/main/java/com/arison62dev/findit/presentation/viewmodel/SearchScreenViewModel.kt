package com.arison62dev.findit.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arison62dev.findit.domain.model.Post
import com.arison62dev.findit.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.arison62dev.findit.data.Result
import com.arison62dev.findit.data.mapper.toDomain
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Post>>(emptyList())
    val searchResults: StateFlow<List<Post>> = _searchResults.asStateFlow()

    private var searchJob: Job? = null

    fun searchPosts(query: String) {
        // Annule la recherche précédente si elle est en cours
        searchJob?.cancel()

        // Ne pas lancer de recherche pour moins de 3 caractères
        if (query.length < 3) {
            _searchResults.value = emptyList()
            _error.value = null
            return
        }

        searchJob = viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            // Ajout d'un délai pour le debounce
            delay(300) // Attendre 300ms après le dernier caractère saisi

            when (val result = postRepository.searchPostsByTitle(query)) {
                is Result.Success -> {
                    _searchResults.value = result.data.map { it.toDomain() }
                }
                is Result.Error -> {
                    _error.value = result.exception.message ?: "Échec de la recherche"
                    _searchResults.value = emptyList()
                }
            }
            _isLoading.value = false
        }
    }

    fun clearSearch() {
        searchJob?.cancel()
        _searchResults.value = emptyList()
        _error.value = null
        _isLoading.value = false
    }
}