package com.kedokato.lession6.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.compose.getCurrentColorScheme
import com.kedokato.lession6.R
import com.kedokato.lession6.domain.model.User
import com.kedokato.lession6.presentation.component.DisconnectInternet
import com.kedokato.lession6.presentation.component.LottieAnimationLoading
import com.kedokato.lession6.presentation.home.component.AlbumItem
import com.kedokato.lession6.presentation.home.component.HeaderScreen
import com.kedokato.lession6.presentation.home.component.Subtitle
import com.kedokato.lession6.presentation.home.component.Title
import com.kedokato.lession6.presentation.home.component.TopAlbums
import com.kedokato.lession6.presentation.home.component.TopAlbumsContent
import com.kedokato.lession6.presentation.home.component.TopArtistContent
import com.kedokato.lession6.presentation.home.component.TopTracksContent
import org.koin.androidx.compose.koinViewModel


@Composable
fun HomeScreen(
    modifier: Modifier,
    onSettingsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onNavigationDetailTopAlbums: () -> Unit,
    onNavigationDetailTopTracks: () -> Unit = {},
) {

    val homeVM: HomeViewModel = koinViewModel()

    val homeState = homeVM.homeState.collectAsState().value

    LaunchedEffect(Unit) {
        homeVM.processIntent(HomeIntent.LoadTopAlbums)
        homeVM.processIntent(HomeIntent.LoadTopTracks)
        homeVM.processIntent(HomeIntent.LoadTopArtists)
        homeVM.processIntent(HomeIntent.LoadUserProfile)
    }


    HomeScreenContent(
        modifier = modifier
            .fillMaxSize()
            .background(getCurrentColorScheme().background),
        onNavigationSettings = onSettingsClick,
        oNavigationProfile = onProfileClick,
        user = null,
        state = homeState,
        onRetry = { homeVM.processIntent(HomeIntent.RetryLoading) },
        onNavigationDetailTopAlbums = {
            onNavigationDetailTopAlbums()
        },
        onNavigationDetailTopTracks = {
            onNavigationDetailTopTracks()
        }

    )

}


@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    onNavigationSettings: () -> Unit = {},
    oNavigationProfile: () -> Unit = {},
    user: User? = null,
    state: HomeState,
    onRetry: () -> Unit = {},
    onNavigationDetailTopAlbums: () -> Unit = {},
    onNavigationDetailTopTracks: () -> Unit = {},
) {
    LazyColumn(modifier = modifier) {
        item {
            HeaderScreen(
                modifier = Modifier.padding(8.dp),
                onNavigationSettings = onNavigationSettings,
                oNavigationProfile = oNavigationProfile,
                user = state.userProfile
            )
        }

        if (state.isNetworkError) {
            item {
                DisconnectInternet(
                    modifier = Modifier.fillMaxSize(),
                    onRetry = onRetry
                )
            }
        } else {
            if (state.isLoading) {
                item {
                    LottieAnimationLoading(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    )
                }
            } else {
                item {
                    Subtitle(
                        modifier = Modifier.padding(8.dp),
                    )
                }

                item {
                    Title(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        title = stringResource(R.string.top_albums),
                        onClick = onNavigationDetailTopAlbums
                    )
                }

                item {
                    TopAlbumsContent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        homeState = state
                    )
                }

                item {
                    Title(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        title = stringResource(R.string.top_tracks),
                        onClick = onNavigationDetailTopTracks
                    )
                }

                item {
                    TopTracksContent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        state = state
                    )
                }

                item {
                    Title(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        title = stringResource(R.string.top_artists)
                    )
                }

                item {
                    TopArtistContent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        state = state
                    )
                }
            }
        }
    }
}

