package com.arison62dev.findit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.arison62dev.findit.presentation.component.FindITAnimatedEditFAB
import com.arison62dev.findit.presentation.component.FindITBottomAppBar
import com.arison62dev.findit.presentation.component.FindITTopAppBar
import com.arison62dev.findit.presentation.navigation.FindITNavGraph
import com.arison62dev.findit.presentation.navigation.Screen
import com.arison62dev.findit.presentation.ui.theme.FindITTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            FindITTheme {
                val navController = rememberNavController()
                val currentBackStack by navController.currentBackStackEntryAsState()
                val currentDestination = currentBackStack?.destination
                val currentScreen = findScreen(currentDestination?.route)

                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    when (currentScreen) {
                        is Screen.LoginScreen -> {}
                        is Screen.SignUpScreen -> {}
                        is Screen.SplashScreen -> {}
                        else -> {
                            FindITTopAppBar(
                                navController = navController,
                                currentScreen = currentScreen ?: Screen.HomeScreen
                            )
                        }
                    }
                },

                    floatingActionButton = {
                        FindITAnimatedEditFAB(
                            onClick = {
                                navController.navigate(Screen.CreatePostScreen.route)
                            }, visible = currentScreen == Screen.HomeScreen
                        )
                    }, bottomBar = {
                        if (currentScreen !is Screen.LoginScreen && currentScreen !is Screen.SignUpScreen && currentScreen !is Screen.SplashScreen && currentScreen !is Screen.CreatePostScreen && currentScreen !is Screen.PostDetailsScreen && currentScreen !is Screen.NotificationsScreen) {
                            FindITBottomAppBar(navController = navController)
                        }
                    }

                ) { innerPadding ->
                    FindITNavGraph(
                        modifier = Modifier.padding(innerPadding), navController = navController
                    )
                }
            }
        }
    }
}

// Helper function to find the current screen from route
private fun findScreen(route: String?): Screen? {
    return when {
        route == null -> null
        route.startsWith(Screen.PostDetailsScreen.route) -> Screen.PostDetailsScreen
        route.startsWith(Screen.ProfileScreen.route) -> Screen.ProfileScreen
        route.startsWith(Screen.ChatScreen.route) -> Screen.ChatScreen
        route.startsWith(Screen.DiscussionScreen.route) -> Screen.DiscussionScreen
        else -> Screen::class.sealedSubclasses.find { it.objectInstance?.route == route }?.objectInstance
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FindITTheme {
        FindITNavGraph()
    }
}