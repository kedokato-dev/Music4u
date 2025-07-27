package com.kedokato.lession6.navigation
sealed class AppScreens(val route: String) {
    object Splash : AppScreens("splash")
    object Login : AppScreens("login?username={username}&password={password}") {
        fun createRoute(username: String = "", password: String = "") =
            "login?username=$username&password=$password"
    }
    object SignUp : AppScreens("signup")
    object Profile : AppScreens("profile")
    object Playlist : AppScreens("playlist")
}