package com.arison62dev.findit.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
        defaultElevation = 4.dp,
        pressedElevation = 2.dp
    )
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(durationMillis = 150)) +
                scaleIn(initialScale = 0.8f, animationSpec = tween(durationMillis = 250)),
        exit = fadeOut(animationSpec = tween(durationMillis = 150)) +
                scaleOut(targetScale = 0.8f, animationSpec = tween(durationMillis = 250)),
        modifier = modifier
    ) {
        FloatingActionButton(
            onClick = onClick,
            containerColor = backgroundColor,
            elevation = elevation,
            modifier = Modifier.animateContentSize(),
            content = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    icon()
                }
            }
        )
    }
}