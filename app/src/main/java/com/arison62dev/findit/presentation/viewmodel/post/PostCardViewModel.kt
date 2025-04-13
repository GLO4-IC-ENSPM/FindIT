package com.arison62dev.findit.presentation.viewmodel.post

import androidx.lifecycle.ViewModel
import com.arison62dev.findit.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Private

@HiltViewModel
class PostCardViewModel @Inject constructor(
    private  val postRepository: PostRepository
) : ViewModel(){
}