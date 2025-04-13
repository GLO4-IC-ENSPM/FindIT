package com.arison62dev.findit.presentation.viewmodel.post

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arison62dev.findit.data.Result
import com.arison62dev.findit.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PostImageState(val imageUrl: String? = null)

@HiltViewModel
class PostImageViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {
    val postImageState = mutableStateOf(PostImageState())
    val isLoading = mutableStateOf(true)

    fun getImageByPostId(postId: Int){
        viewModelScope.launch {
            isLoading.value = true
            when(val reslut = postRepository.getImagePostByPostId(postId)){
                is Result.Success -> {
                    postImageState.value = PostImageState(reslut.data.chemin)
                    isLoading.value = false
                    Log.d("getImageByPostId", "Image chargée avec succès ${reslut.data.chemin}")
                }
                is Result.Error -> {
                    isLoading.value = false
                }
            }

        }
    }

}