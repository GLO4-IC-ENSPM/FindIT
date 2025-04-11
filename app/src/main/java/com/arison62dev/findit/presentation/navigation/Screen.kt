package com.arison62dev.findit.presentation.navigation

sealed class Screen(val route : String){
    object SplashScreen : Screen("splash_screen")
    object LoginScreen : Screen("login_screen")
    object SignUpScreen : Screen("sign_up_screen")
    object HomeScreen : Screen("home_screen")
    object SearchScreen : Screen("search_screen")
    object NotificationsScreen : Screen("notifications_screen")
    object CreatePostScreen : Screen("create_post_screen")
    object EditProfileScreen : Screen("edit_profile_screen")
    object BookmarksScreen : Screen("bookmarks_screen")
    object SettingsScreen : Screen("settings_screen")

    object PostDetailsScreen : Screen("post_details_screen/{postId}"){
        fun createRoute(postId : Int) = "post_details_screen/$postId"
    }
    object ProfileScreen : Screen("profile_screen/{userId}"){
        fun createRoute(userId : Int) = "profile_screen/$userId"
    }
   object  ChatScreen : Screen("chat_screen/{userId}"){
       fun createRoute(userId : Int) = "chat_screen/$userId"
   }
    object DiscussionScreen : Screen("discussion_screen/{discussionId}"){
        fun createRoute(discussionId : Int) = "discussion_screen/$discussionId"
    }
}