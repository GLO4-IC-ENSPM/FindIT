package com.arison62dev.findit.presentation.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.arison62dev.findit.presentation.navigation.Screen
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindITTopAppBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    currentScreen: Screen,
    title: String? = null,
    actions: @Composable () -> Unit = {}
) {
    val showBackArrow = when (currentScreen) {
        Screen.HomeScreen, Screen.SplashScreen, Screen.LoginScreen, Screen.SignUpScreen, Screen.SearchScreen, Screen.BookmarksScreen, Screen.ProfileScreen -> false
        else -> true
    }

    val navigationIcon: @Composable () -> Unit = {
        if (showBackArrow) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }

    val defaultActions: @Composable RowScope.() -> Unit = {
        when (currentScreen) {
            Screen.HomeScreen -> {
                IconButton(onClick = { navController.navigate(Screen.NotificationsScreen.route) }) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            else -> actions()
        }
    }

    val screenTitle = title ?: when (currentScreen) {
        Screen.HomeScreen -> "Home"
        Screen.CreatePostScreen -> "Create Post"
        Screen.EditProfileScreen -> "Edit Profile"
        Screen.PostDetailsScreen -> "Post Details"
        Screen.SettingsScreen -> "Settings"
        Screen.NotificationsScreen -> "Notifications"
        Screen.SearchScreen -> "Search"
        Screen.BookmarksScreen -> "Bookmarks"
        else -> currentScreen.route.split('_').joinToString(" ") { it.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        } }
    }

    LargeTopAppBar(
        modifier = modifier,
        title = { Text(text = screenTitle, style = MaterialTheme.typography.headlineSmall) },
        navigationIcon = navigationIcon,
        actions = defaultActions,
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FindITTopAppBarPreview(

) {
    FindITTopAppBar(
        navController = rememberNavController(),
        currentScreen = Screen.CreatePostScreen,

        )
}


