package com.kedokato.lession6.presentation.home.top_tracks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kedokato.lession6.R
import com.kedokato.lession6.domain.model.TrackInfo
import com.kedokato.lession6.presentation.home.component.MyTopAppBar
import com.kedokato.lession6.presentation.home.component.TrackItem
import com.kedokato.lession6.presentation.home.component.trackColors
import org.koin.androidx.compose.koinViewModel

@Composable
fun TopTracksDetailScreen(modifier: Modifier = Modifier, onNavigationIconClick: () -> Unit = {}) {

    val viewModel: TopTracksViewModel = koinViewModel()
    val state = viewModel.state.collectAsState().value

    Scaffold(
        topBar = {
            MyTopAppBar(
                modifier = modifier,
                title = R.string.top_app_bar_top_tracks,
                onNavigationIconClick = onNavigationIconClick,
            )
        }
    ) { innerPadding ->
        TopTracksContent(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            state = state,
        )
    }

}


@Composable
fun TopTracksContent(
    modifier: Modifier = Modifier,
    state: TopTracksDetailState,
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp),
    ) {

        items(state.tracks.size) { index ->
            val color = trackColors[index % trackColors.size]
            TrackItem(
                modifier = Modifier.width(160.dp),
                color = color,
                imageUrl = state.tracks[index].imageUrl,
                trackName = state.tracks[index].name,
                listenerCount = state.tracks[index].countListeners,
                albumName = state.tracks[index].artist
            )
        }
    }
}

@Preview
@Composable
private fun TopTracksContentPreview() {
    TopTracksContent(
        modifier = Modifier,
        state = TopTracksDetailState(
            tracks = listOf(
                TrackInfo(
                    name = "Track 1",
                    artist = "Artist 1",
                    imageUrl = "https://example.com/image1.jpg",
                    countListeners = "1000"
                ),
                TrackInfo(
                    name = "Track 2",
                    artist = "Artist 2",
                    imageUrl = "https://example.com/image2.jpg",
                    countListeners = "2000"
                ),
                TrackInfo(
                    name = "Track 2",
                    artist = "Artist 2",
                    imageUrl = "https://example.com/image2.jpg",
                    countListeners = "2000"
                ),
                TrackInfo(
                    name = "Track 2",
                    artist = "Artist 2",
                    imageUrl = "https://example.com/image2.jpg",
                    countListeners = "2000"
                )
            )
        ),
    )
}

val trackColors = listOf(
    Color(0xFFFF7777), // RedSoft
    Color(0xFFFFFA77), // YellowSoft
    Color(0xFF4462FF), // BlueBright
    Color(0xFF14FF00), // GreenNeon
    Color(0xFFE231FF), // PurpleBright
    Color(0xFF00FFFF), // CyanBright
    Color(0xFFFB003C), // PinkDeep
    Color(0xFFF2A5FF)  // PurpleLight
)