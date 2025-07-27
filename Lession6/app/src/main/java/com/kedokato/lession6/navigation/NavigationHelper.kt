package com.kedokato.lession6.navigation

import androidx.navigation.NavController

class NavigationHelper(private val navController: NavController) {

    fun navigateToLogin() {
        navController.navigate(AppScreens.Login.route) {
            popUpTo(0) { inclusive = true }
        }
    }

    fun navigateToSignUp() {
        navController.navigate(AppScreens.SignUp.route)
    }

    fun navigateToProfile() {
        navController.navigate(AppScreens.Profile.route) {
            popUpTo(AppScreens.Login.route) { inclusive = true }
        }
    }

    fun navigateToPlaylist() {
        navController.navigate(AppScreens.Playlist.route)
    }

    fun navigateBack() {
        navController.popBackStack()
    }

    fun navigateToSplash() {
        navController.navigate(AppScreens.Splash.route) {
            popUpTo(0) { inclusive = true }
        }
    }

    fun navigateToLoginWithCredentials(username: String, password: String) {
        navController.navigate(AppScreens.Login.createRoute(username, password)) {
            popUpTo(AppScreens.SignUp.route) { inclusive = true }
        }
    }
}