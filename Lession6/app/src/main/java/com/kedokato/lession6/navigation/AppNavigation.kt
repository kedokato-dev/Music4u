package com.kedokato.lession6.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.kedokato.lession6.presentation.home.HomeScreen
import com.kedokato.lession6.presentation.login.LoginScreen
import com.kedokato.lession6.presentation.playlist.myplaylist.MyPlaylistScreen
import com.kedokato.lession6.presentation.profile.ProfileScreen
import com.kedokato.lession6.presentation.signup.SignUpScreen
import com.kedokato.lession6.presentation.splash.SplashScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
) {
    val backStack = rememberNavBackStack<RememberScreen>(
        RememberScreen.LoginScreen(
            username = "",
            password = ""
        )
//        RememberScreen.NestedGraph
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
                    initialUsername = entry.username,
                    initialPassword = entry.password,
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
                    onBackClick = {
                        backStack.removeLastOrNull()
                    },
                    onSignUpClick = { username, password ->
                        // Xóa màn hình SignUpScreen khỏi backstack
                        backStack.removeLastOrNull()
                        // Xóa luôn màn hình LoginScreen cũ khỏi backstack
                        backStack.removeLastOrNull()
                        // Thêm lại màn hình LoginScreen mới với dữ liệu đã được cập nhật
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
                ProfileScreen(
                    isEditMode = false,
                    isDarkTheme = true,
                    modifier = modifier,
                    onBackClick = {
                        backStack.removeLastOrNull()
                    }

                )
            }

            entry<RememberScreen.PlaylistScreen> {
                MyPlaylistScreen()
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