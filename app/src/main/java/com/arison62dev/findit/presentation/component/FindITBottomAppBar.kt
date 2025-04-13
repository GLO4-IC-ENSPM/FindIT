package com.arison62dev.findit.presentation.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.arison62dev.findit.presentation.navigation.Screen

@Composable
fun FindITBottomAppBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val primaryColor = MaterialTheme.colorScheme.primary
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant

    BottomAppBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface,
        actions = {
            NavigationBarItem(
                alwaysShowLabel = false,
                selected = currentRoute == Screen.HomeScreen.route,
                onClick = { navController.navigate(Screen.HomeScreen.route){
                    launchSingleTop = true
                    popUpTo(Screen.HomeScreen.route){
                        saveState = true
                    }
                    restoreState = true
                } },
                icon = {
                    Icon(
                        imageVector = if (currentRoute == Screen.HomeScreen.route) {
                            Icons.Filled.Home
                        } else {
                            Icons.Outlined.Home
                        },
                        contentDescription = "Posts",
                        tint = if (currentRoute == Screen.HomeScreen.route) primaryColor else onSurfaceVariant
                    )
                },
                label = { Text("Posts") }
            )

            NavigationBarItem(
                alwaysShowLabel = false,
                selected = currentRoute == Screen.SearchScreen.route,
                onClick = { navController.navigate(Screen.SearchScreen.route){
                    launchSingleTop = true
                    popUpTo(Screen.HomeScreen.route){
                        saveState = true
                    }
                    restoreState = true
                } },
                icon = {
                    Icon(
                        imageVector = if (currentRoute == Screen.SearchScreen.route) {
                            Icons.Filled.Search
                        } else {
                            Icons.Outlined.Search
                        },
                        contentDescription = "Recherche",
                        tint = if (currentRoute == Screen.SearchScreen.route) primaryColor else onSurfaceVariant
                    )
                },
                label = { Text("Recherche") }
            )

            NavigationBarItem(
                alwaysShowLabel = false,
                selected = currentRoute == Screen.BookmarksScreen.route,
                onClick = { navController.navigate(Screen.BookmarksScreen.route){
                    launchSingleTop = true
                    popUpTo(Screen.HomeScreen.route){
                        saveState = true
                    }
                    restoreState = true
                } },
                icon = {
                    Icon(
                        imageVector = if (currentRoute == Screen.BookmarksScreen.route) {
                            Icons.Filled.Bookmark
                        } else {
                            Icons.Outlined.Bookmark
                        },
                        contentDescription = "Enregistrements",
                        tint = if (currentRoute == Screen.BookmarksScreen.route) primaryColor else onSurfaceVariant
                    )
                },
                label = { Text("Enregistrements") }
            )

            NavigationBarItem(
                alwaysShowLabel = false,
                selected = currentRoute == Screen.ProfileScreen.route,
                onClick = { navController.navigate(Screen.ProfileScreen.route){
                    launchSingleTop = true
                    popUpTo(Screen.HomeScreen.route){
                        saveState = true
                    }
                    restoreState = true
                } },
                icon = {
                    Icon(
                        imageVector = if (currentRoute == Screen.ProfileScreen.route) {
                            Icons.Filled.Person
                        } else {
                            Icons.Outlined.Person
                        },
                        contentDescription = "Profile",
                        tint = if (currentRoute == Screen.ProfileScreen.route) primaryColor else onSurfaceVariant
                    )
                },
                label = { Text("Profile") }
            )
        }
    )
}