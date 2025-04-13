package com.arison62dev.findit.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arison62dev.findit.data.Result
import com.arison62dev.findit.data.local.AuthDataStore
import com.arison62dev.findit.domain.model.Post
import com.arison62dev.findit.domain.model.PostStatut
import com.arison62dev.findit.domain.model.PostType
import com.arison62dev.findit.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val authDataStore: AuthDataStore
) : ViewModel() {

    // État du post en cours de création
    private  val _postState = mutableStateOf(
        Post(
            titre = "",
            type = PostType.PERDU,
            estAnonyme = false,
            nbSignalements = 0,
            nbLikes = 0,
            statut = PostStatut.OUVERT,
            dateHeure = LocalDateTime.now(),
            idUtilisateur = null,
            idPost = null,
            datePublication = LocalDateTime.now()
        )
    )
    val postState: State<Post> = _postState

    // États UI
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)
    val isSuccess = mutableStateOf(false)

    // Mettre à jour les propriétés du post
    fun updateTitle(title: String) {
        _postState.value = postState.value.copy(titre = title)
    }

    fun updateType(type: PostType) {
        _postState.value = postState.value.copy(type = type)
    }

    fun updateIsAnonymous(isAnonymous: Boolean) {
        _postState.value = postState.value.copy(estAnonyme = isAnonymous)
    }



    // Créer le post avec une image
    suspend fun createPostWithImage(imageName: String, imageFile: ByteArray) {
            isLoading.value = true
            errorMessage.value = null
            isSuccess.value = false
        viewModelScope.launch {
            try {
                // Validation
                when {
                    postState.value.titre.isBlank() -> {
                        errorMessage.value = "Le titre est requis"
                    }
                    imageFile.isEmpty() -> {
                        errorMessage.value = "Veuillez sélectionner une image"
                    }
                }

                // Récupérer l'ID utilisateur
                val userId = authDataStore.userId.first() ?: run {
                    errorMessage.value = "Utilisateur non connecté"
                }

                // Créer le post
                val result = postRepository.createPost(
                    post = postState.value,
                    imageName = imageName,
                    imageFile = imageFile
                )
                Log.d("CreatePostViewModel", "Result: $result")
                if (result is Result.Success) {
                    isSuccess.value = true

                } else if(result is Result.Error) {
                    errorMessage.value = result.exception.message ?: "Une erreur est survenue"
                }
            } catch (e: Exception) {
                errorMessage.value = "Erreur: ${e.message}"
            } finally {
                isLoading.value = false
                Log.d("CreatePostViewModel", "Post créé avec succès ${isLoading.value}")
            }
        }
    }

    fun resetForm() {
        _postState.value = Post(
            titre = "",

            type = PostType.PERDU,
            estAnonyme = false,
            nbSignalements = 0,
            nbLikes = 0,
            statut = PostStatut.OUVERT,
            dateHeure = LocalDateTime.now(),
            idUtilisateur = null,
            idPost = null,
            datePublication = LocalDateTime.now()
        )
        errorMessage.value = null
        isSuccess.value = false
    }

}