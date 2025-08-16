package com.kedokato.lession6.presentation.home.top_albums

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kedokato.lession6.R
import com.kedokato.lession6.domain.model.AlbumInfo
import com.kedokato.lession6.presentation.component.DisconnectInternet
import com.kedokato.lession6.presentation.component.LottieAnimationLoading
import com.kedokato.lession6.presentation.home.component.AlbumItemForList
import com.kedokato.lession6.presentation.home.component.MyTopAppBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun TopAlbumsDetailScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    val viewModel: TopAlbumsViewModel = koinViewModel()
    val state = viewModel.state.collectAsState().value

    TopAlbumsDetailContent(
        modifier = modifier,
        state = state,
        onBackClick = onBackClick,
        onRetry = { viewModel.retry() }
    )
}

@Composable
fun TopAlbumsDetailContent(
    modifier: Modifier = Modifier,
    state: TopAlbumsDetailState,
    onBackClick: () -> Unit,
    onRetry: () -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
    ) {
        item {
            MyTopAppBar(
                modifier = modifier,
                title = R.string.top_app_bar_top_albums,
                onNavigationIconClick = onBackClick
            )
        }

        if (state.isNetworkError) {
            item {
                DisconnectInternet(
                    modifier = Modifier.fillMaxSize(),
                    onRetry = onRetry
                )
            }
        } else if (state.isLoading) {
            item {
                LottieAnimationLoading(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            }
        } else {
            items(state.albums.size) { index ->
                AlbumItemForList(
                    modifier = modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
                    titleAlbum = state.albums[index].name,
                    artistName = state.albums[index].artist,
                    imageUrl = state.albums[index].imageUrl,
                )
            }
        }
    }
}


val fakeState: TopAlbumsDetailState = TopAlbumsDetailState(
    isLoading = false,
    error = null,
    isNetworkError = false,
    albums = listOf(
        AlbumInfo(
            name = "Album 1",
            artist = "Artist 1",
            imageUrl = "https://example.com/image1.jpg"
        ),
        AlbumInfo(
            name = "Album 2",
            artist = "Artist 2",
            imageUrl = "https://example.com/image2.jpg"
        )
    )
)

@Preview(showBackground = true)
@Composable
private fun TopAlbumsDetailScreenPreview() {
    TopAlbumsDetailContent( modifier = Modifier,  state = fakeState, onBackClick = {}, onRetry = {} )
}
