package com.arison62dev.findit.presentation.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.arison62dev.findit.R
import com.arison62dev.findit.presentation.navigation.Screen
import com.arison62dev.findit.presentation.viewmodel.SplashViewModel
import kotlinx.coroutines.delay

@Composable
fun FindItSplashScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val splashViewModel: SplashViewModel = hiltViewModel()
    val startDestination by splashViewModel.startDestination.collectAsState()

    // Animations
    val logoAlpha = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }
    val yOffset = remember { Animatable(20f) }
    val pulseAnim = remember { Animatable(1f) }

    // Animation de pulsation infinie pour le logo
    LaunchedEffect(Unit) {
        pulseAnim.animateTo(
            targetValue = 1.05f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    // Animation principale
    LaunchedEffect(Unit) {
        // Animation d'entrée du logo
        logoAlpha.animateTo(1f, animationSpec = tween(800))
        yOffset.animateTo(0f, animationSpec = tween(600))

        // Délai avant l'apparition du texte
        delay(300)
        textAlpha.animateTo(1f, animationSpec = tween(500))

        // Délai avant la transition
        delay(1500)
        logoAlpha.animateTo(0f, animationSpec = tween(500))
        textAlpha.animateTo(0f, animationSpec = tween(500))

        // Transition vers l'écran suivant
        navController.navigate(startDestination) {
            popUpTo(Screen.SplashScreen.route) { inclusive = true }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            // Logo avec animation de pulsation
            Image(
                painter = painterResource(id = R.drawable.findit_logo_complet),
                contentDescription = stringResource(R.string.app_name),
                modifier = Modifier
                    .size(120.dp)
                    .scale(pulseAnim.value) // Extension scale à créer
                    .alpha(logoAlpha.value)
                    .offset(y = yOffset.value.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Texte avec la devise
            Text(
                text = stringResource(R.string.app_motto),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                modifier = Modifier
                    .alpha(textAlpha.value)
            )
        }
    }
}

// Extension pour le scale
fun Modifier.scale(scale: Float) = this.scale(scale)
