package com.arison62dev.findit.presentation.component.post

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.arison62dev.findit.presentation.viewmodel.post.PostImageViewModel

@Composable
fun PostImage(
    modifier: Modifier = Modifier,
    postId: Int,
    viewModel: PostImageViewModel = hiltViewModel()
) {
    val postImageState by viewModel.postImageState
    val isLoading by viewModel.isLoading

    LaunchedEffect(postId) {
        viewModel.getImageByPostId(postId)
    }

    if (isLoading) {
        PostImageSkeleton()
    } else {
        var hasErrorImage by remember { mutableStateOf(false) }
        Column(
            modifier = modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Image du post
            AsyncImage(
                model = postImageState.imageUrl ?: "",
                contentDescription = "Image du post",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
                onError = {
                    hasErrorImage = true
                }
            )
        }
    }

}

@Composable
fun PostImageSkeleton() {
    // Animation pour l'effet de shimmer
    val shimmerColors = listOf(
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, delayMillis = 300),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(10f, 10f),
        end = Offset(translateAnim.value, translateAnim.value)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp)
            .background(brush)
    )
}

/**
 * Placeholder en cas d'erreur de chargement de l'image.
 */
@Composable
fun ErrorPlaceholderImage() {
    Icon(
        imageVector = Icons.Default.Image,
        contentDescription = "Erreur de chargement de l'image",
        modifier = Modifier
            .size(60.dp)
            .padding(8.dp),
        tint = MaterialTheme.colorScheme.onSurfaceVariant
    )
}