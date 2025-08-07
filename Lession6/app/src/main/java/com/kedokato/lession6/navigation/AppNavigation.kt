package com.kedokato.lession6.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.kedokato.lession6.domain.usecase.GetUserIdUseCaseShared
import com.kedokato.lession6.presentation.home.HomeScreen
import com.kedokato.lession6.presentation.login.LoginScreen
import com.kedokato.lession6.presentation.playlist.myplaylist.MyPlaylistScreen
import com.kedokato.lession6.presentation.profile.ProfileScreen
import com.kedokato.lession6.presentation.signup.SignUpScreen
import com.kedokato.lession6.presentation.splash.SplashScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.compose.koinInject

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
) {
    val getUserIdUseCase: GetUserIdUseCaseShared = koinInject()
    var isCheckingUser by remember { mutableStateOf(true) }
    var hasValidUser by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val savedUserId = withContext(Dispatchers.IO) {
            getUserIdUseCase.invoke()
        }
        savedUserId?.let { hasValidUser = it > 0 }
        isCheckingUser = false
    }

    val initialScreen = when {
        isCheckingUser -> RememberScreen.NestedGraph
        hasValidUser -> RememberScreen.NestedGraph
        else -> RememberScreen.LoginScreen(username = "", password = "")
    }

    val backStack = rememberNavBackStack<RememberScreen>(initialScreen)

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
                        backStack.add(RememberScreen.SignUpScreen)
                    },
                    onLoginClick = {
                        backStack.clear()
                        backStack.add(RememberScreen.NestedGraph)
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
                        backStack.removeLastOrNull()
                        backStack.removeLastOrNull()
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
                        backStack.add(RememberScreen.ProfileScreen)
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
                        backStack.add(RememberScreen.ProfileScreen)
                    }
                )
            }
        }
    )
}