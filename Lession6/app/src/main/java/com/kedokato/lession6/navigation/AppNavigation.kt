package com.kedokato.lession6.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kedokato.lession6.view.login.LoginScreen
import com.kedokato.lession6.view.signup.SignUpScreen
import com.kedokato.lession6.view.splash.SplashScreen
import kotlinx.coroutines.delay

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navigationHelper = remember { NavigationHelper(navController) }

    NavHost(
        navController = navController,
        startDestination = AppScreens.Splash.route
    ) {
        composable(AppScreens.Splash.route) {
            SplashScreen(
                modifier = androidx.compose.ui.Modifier
            )
            LaunchedEffect(key1 = Unit) {
                delay(2000)
                navigationHelper.navigateToLogin()
            }
        }


        composable(
            route = AppScreens.Login.route,
            arguments = listOf(
                navArgument("username") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("password") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            val password = backStackEntry.arguments?.getString("password") ?: ""

            LoginScreen(
                modifier = androidx.compose.ui.Modifier,
                initialUsername = username,
                initialPassword = password,
                onSignUpClick = { navigationHelper.navigateToSignUp() },
                onLoginClick = { navigationHelper.navigateToPlaylist() }
            )
        }

        composable(AppScreens.SignUp.route) {
            SignUpScreen(
                modifier = androidx.compose.ui.Modifier,
                onBackClick = { navigationHelper.navigateBack() },
                onSignUpClick = { username, password ->
                    navigationHelper.navigateToLoginWithCredentials(username, password)
                }
            )
        }

        composable(AppScreens.Profile.route) {
            // Profile screen will be implemented later
        }

        composable(AppScreens.Playlist.route) {
            // Playlist screen will be implemented later
        }
    }
}