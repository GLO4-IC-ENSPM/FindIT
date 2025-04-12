package com.arison62dev.findit.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arison62dev.findit.R
import com.arison62dev.findit.presentation.ui.theme.FindITTheme
import kotlinx.coroutines.delay

@Composable
fun LoginSuccessScreen(
    email: String,
    createdAt: String,
    onContinue: () -> Unit
) {
    val transition = rememberInfiniteTransition(label = "")
    val pulseAnim by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val slideInAnim = remember { Animatable(100f) }
    val fadeInAnim = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        slideInAnim.animateTo(0f, animationSpec = tween(500))
        fadeInAnim.animateTo(1f, animationSpec = tween(800))
    }

    FindITTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Logo animé
                Image(
                    painter = painterResource(id = R.drawable.findit_logo),
                    contentDescription = "FindIT Logo",
                    modifier = Modifier
                        .size(150.dp)
                        .scale(pulseAnim)
                        .graphicsLayer {
                            alpha = fadeInAnim.value
                            translationY = slideInAnim.value
                        }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Checkmark animé
                var checkmarkVisible by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    delay(500)
                    checkmarkVisible = true
                }

                AnimatedVisibility(
                    visible = checkmarkVisible,
                    enter = scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut()
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(64.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Contenu textuel animé
                Column(
                    modifier = Modifier
                        .graphicsLayer {
                            alpha = fadeInAnim.value
                            translationY = slideInAnim.value * 0.8f
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Connexion réussie!",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Détails utilisateur avec animation séquentielle
                    var detailsVisible by remember { mutableStateOf(false) }

                    LaunchedEffect(Unit) {
                        delay(300)
                        detailsVisible = true
                    }

                    AnimatedVisibility(
                        visible = detailsVisible,
                        enter = slideInVertically { it / 2 } + fadeIn(),
                        exit = slideOutVertically() + fadeOut()
                    ) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(
                                text = "Email: $email",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Compte créé le: $createdAt",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Bouton animé
                var buttonVisible by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    delay(800)
                    buttonVisible = true
                }

                AnimatedVisibility(
                    visible = buttonVisible,
                    enter = scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut()
                ) {
                    Button(
                        onClick = onContinue,
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text("Continuer vers l'application")
                    }
                }
                val colorScheme = MaterialTheme.colorScheme
                // Animation de fond discrète
                Canvas(
                    modifier = Modifier.fillMaxSize(),
                    onDraw = {
                        drawCircle(
                            color = colorScheme.primary.copy(alpha = 0.05f),
                            radius = size.minDimension * pulseAnim * 0.5f,
                            center = center
                        )
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginSuccessScreenPreview(){
    LoginSuccessScreen(email = "example.com", createdAt = "") {

    }
}