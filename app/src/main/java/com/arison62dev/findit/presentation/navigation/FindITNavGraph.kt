package com.arison62dev.findit.presentation.navigation

import EditProfileScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arison62dev.findit.presentation.screen.BookmarksScreen
import com.arison62dev.findit.presentation.screen.ChatScreen
import com.arison62dev.findit.presentation.screen.CreatePostScreen
import com.arison62dev.findit.presentation.screen.DiscussionScreen
import com.arison62dev.findit.presentation.screen.FindItSplashScreen
import com.arison62dev.findit.presentation.screen.HomeScreen
import com.arison62dev.findit.presentation.screen.LoginScreen
import com.arison62dev.findit.presentation.screen.NotificationScreen
import com.arison62dev.findit.presentation.screen.PostDetailsScreen
import com.arison62dev.findit.presentation.screen.ProfileScreen
import com.arison62dev.findit.presentation.screen.SearchScreen
import com.arison62dev.findit.presentation.screen.SettingsScreen
import com.arison62dev.findit.presentation.screen.SignUpScreen


@Composable
fun FindITNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.SplashScreen.route
    ) {
        composable(Screen.SplashScreen.route) {
            FindItSplashScreen(navController = navController)
        }

        composable(Screen.LoginScreen.route) {
            LoginScreen(navController = navController, onLoginSuccess = {})
        }

        composable(Screen.HomeScreen.route) {
            HomeScreen()
        }

        composable(Screen.SearchScreen.route) {
            SearchScreen()
        }

        composable(Screen.CreatePostScreen.route) {
            CreatePostScreen(navController = navController)
        }

        composable(Screen.NotificationsScreen.route) {
            NotificationScreen()
        }
        composable(Screen.PostDetailsScreen.route) { navBackStackEntry ->
            val postId = navBackStackEntry.arguments?.getInt("postId")
            if (postId != null) {
                PostDetailsScreen(postId = postId, navController)
            }
        }
        composable(Screen.ProfileScreen.route) { navBackStackEntry ->
            val userId = navBackStackEntry.arguments?.getInt("userId")
            if (userId != null) {
                ProfileScreen()
            }
        }

        composable(Screen.ChatScreen.route) { navBackStackEntry ->
            val userId = navBackStackEntry.arguments?.getInt("userId")
            if (userId != null) {
                ChatScreen()
            }
        }

        composable(Screen.DiscussionScreen.route) { navBackStackEntry ->
            val discussionId = navBackStackEntry.arguments?.getInt("discussionId")
            if (discussionId != null) {
                DiscussionScreen()
            }
        }

        composable(Screen.SettingsScreen.route) {
            SettingsScreen()
        }

        composable(Screen.SignUpScreen.route) {
            SignUpScreen(navController = navController)
        }

        composable(Screen.BookmarksScreen.route) {
            BookmarksScreen()
        }

        composable(Screen.EditProfileScreen.route) {
            EditProfileScreen()
        }

    }
}