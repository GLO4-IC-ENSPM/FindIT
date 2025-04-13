package com.arison62dev.findit.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Composable
fun FindITAnimatedEditFAB(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    visible: Boolean = true,
    icon: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.Default.AddComment,
            contentDescription = "Edit",
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    },
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(
        defaultElevation = 6.dp,
        pressedElevation = 8.dp,
        hoveredElevation = 10.dp,
        focusedElevation = 8.dp
    )
) {
    Box(modifier = modifier) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(
                animationSpec = tween(
                    durationMillis = 200,
                    easing = FastOutSlowInEasing
                )
            ) + scaleIn(
                initialScale = 0.0f,
                transformOrigin = TransformOrigin(0.5f, 0.5f),
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            ) + expandIn(
                expandFrom = Alignment.Center,
                initialSize = { IntSize(0, 0) },
                animationSpec = tween(200)
            ),
            exit = fadeOut(
                animationSpec = tween(
                    durationMillis = 150,
                    easing = FastOutSlowInEasing
                )
            ) + scaleOut(
                targetScale = 0.0f,
                transformOrigin = TransformOrigin(0.5f, 0.5f),
                animationSpec = tween(150)
            ) + shrinkOut(
                shrinkTowards = Alignment.Center,
                targetSize = { IntSize(0, 0) },
                animationSpec = tween(150)
            )
        ) {
            FloatingActionButton(
                onClick = onClick,
                containerColor = backgroundColor,
                elevation = elevation,
                modifier = Modifier
                    .graphicsLayer {
                        if (visible) {
                            shadowElevation = 8f
                        }
                    },
                content = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp)
                    ) {
                        icon()
                    }
                }
            )
        }
    }
}