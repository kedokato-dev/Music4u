package com.kedokato.lession6.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed class RememberScreen: NavKey {
    @Serializable
    @SerialName("HomeScreen")
    data object HomeScreen : RememberScreen()
    @Serializable
    @SerialName("SplashScreen")
    data object SplashScreen : RememberScreen()
    @Serializable
    @SerialName("LoginScreen")
    data class LoginScreen(val username: String, val password: String) : RememberScreen()
    @Serializable
    @SerialName("SignUpScreen")
    data object SignUpScreen : RememberScreen()
    @Serializable
    @SerialName("PlaylistScreen")
    data object PlaylistScreen : RememberScreen()
    @Serializable
    @SerialName("ProfileScreen")
    data object ProfileScreen : RememberScreen()

    @Serializable
    @SerialName("RememberScreen")
    data object NestedGraph : RememberScreen()

}


