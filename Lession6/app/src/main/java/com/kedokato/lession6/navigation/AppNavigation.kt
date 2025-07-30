package com.kedokato.lession6.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.kedokato.lession6.view.home.HomeScreen
import com.kedokato.lession6.view.login.LoginScreen
import com.kedokato.lession6.view.playlist.PlayListScreen
import com.kedokato.lession6.view.profile.ProfileView
import com.kedokato.lession6.view.signup.SignUpScreen
import com.kedokato.lession6.view.splash.SplashScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
) {
    val backStack = rememberNavBackStack<RememberScreen>(
        RememberScreen.LoginScreen(
            username = "",
            password = ""
        )
    )

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<RememberScreen.SplashScreen> {
                SplashScreen(
                    modifier = modifier,
                )
            }

            entry<RememberScreen.LoginScreen> { entry ->
                LoginScreen(
                    modifier = modifier,
                    onSignUpClick = {
                        backStack.add(
                            RememberScreen.SignUpScreen
                        )
                    },
                    onLoginClick = {
                        backStack.clear()
                        backStack.add(
                            RememberScreen.NestedGraph
                        )
                    },
                )
            }

            entry<RememberScreen.SignUpScreen> {
                SignUpScreen(
                    modifier = modifier,
                    onBackClick = backStack::removeLastOrNull,
                    onSignUpClick = { username: String,
                                      password: String ->
                        backStack.add(
                            RememberScreen.LoginScreen(
                                username = username,
                                password = password
                            )
                        )
                    }
                )
            }

            entry<RememberScreen.HomeScreen> {
                HomeScreen(
                    modifier = modifier,
                    onProfileClick = {
                        backStack.add(
                            RememberScreen.ProfileScreen
                        )
                    }
                )
            }

            entry<RememberScreen.ProfileScreen> {
                ProfileView(
                    isEditMode = true,
                    onEditModeChange = {
                        backStack.add(
                            RememberScreen.HomeScreen
                        )
                    },
                    isDarkTheme = true,
                    modifier = modifier,

                )
            }

            entry<RememberScreen.PlaylistScreen> {
                PlayListScreen(
                    typeDisplay = true,
                    isSort = false
                )
            }

            entry<RememberScreen.NestedGraph> {
                NestedGraph(
                    onProfileClick = {
                        backStack.add(
                            RememberScreen.ProfileScreen
                        )
                    }
                )
            }
        }
    )
}