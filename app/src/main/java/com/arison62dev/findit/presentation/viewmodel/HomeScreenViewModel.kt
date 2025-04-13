package com.arison62dev.findit.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arison62dev.findit.data.Result
import com.arison62dev.findit.data.mapper.toDomain
import com.arison62dev.findit.domain.model.Post
import com.arison62dev.findit.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val postRepository: PostRepository,
) : ViewModel() {
    var posts : MutableStateFlow<List<Post>> = MutableStateFlow(emptyList())
    var isLoading : MutableStateFlow<Boolean> = MutableStateFlow(true)
    var isLoadigNextPage : MutableStateFlow<Boolean> = MutableStateFlow(false)
    var error : MutableStateFlow<String?> = MutableStateFlow(null)
    var isPullingToRefresh : MutableStateFlow<Boolean> = MutableStateFlow(false)
    var hasMorePosts : MutableStateFlow<Boolean> = MutableStateFlow(true)
    var offset : MutableStateFlow<Long> = MutableStateFlow(0)

    init {
        viewModelScope.launch {
            getAllPosts()
        }
    }

    private suspend fun getAllPosts(limit: Long = 10, offset: Long = 0){
        isLoading.value = true
        when(val result = postRepository.getAllPosts(limit, offset)){
            is Result.Success -> {
               // add to posts
                posts.value += result.data.map { it.toDomain() }
                this.offset.value = offset + limit
                if(result.data.size < limit){
                    hasMorePosts.value = false
                }
            }
            is Result.Error -> {
                error.value = result.exception.message
            }
        }
    }

    fun pullRefresh(){
        isPullingToRefresh.value = true
        viewModelScope.launch {
            posts.value = emptyList()
            offset.value = 0
            hasMorePosts.value = true
            getAllPosts()
            isPullingToRefresh.value = false
        }
    }

    fun nextPage(){
        if(hasMorePosts.value) {
            isLoadigNextPage.value = true
            viewModelScope.launch {
                getAllPosts(offset = offset.value)
                isLoadigNextPage.value = false
            }
        }else{
            isLoadigNextPage.value = false
            error.value = "Aucun autre post à charger"
        }
    }
}