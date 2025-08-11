package com.kedokato.lession6.presentation.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.compose.getCurrentColorScheme
import com.kedokato.lession6.domain.model.Song
import com.kedokato.lession6.presentation.player.component.ControlPlayer
import com.kedokato.lession6.presentation.player.component.ImageSongContent
import com.kedokato.lession6.presentation.player.component.ImageSongContent2
import com.kedokato.lession6.presentation.player.component.ProgressBarContent
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.compose.viewModel
@Composable
fun PlayerMusicScreen(
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {},
    song: Song
) {
    val playerMusicVM: PlayerMusicViewModel = koinViewModel()
    val playerMusicState = playerMusicVM.playerMusicState.collectAsState().value

    LaunchedEffect(song) {
        playerMusicVM.setSong(song)
        playerMusicVM.playNewSong(song)
    }

    PlayerMusicContent(
        modifier = modifier,
        onNavigationIconClick = onNavigationIconClick,
        song = song,
        viewModel = playerMusicVM,
        states = playerMusicState
    )
}

@Composable
fun PlayerMusicContent(
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {},
    song: Song,
    viewModel: PlayerMusicViewModel? = null,
    states: PlayerMusicState? = null,
) {
    Column(
        modifier = modifier
            .background(getCurrentColorScheme().background)
            .fillMaxSize()
            .padding(32.dp)
    ) {
        Spacer(modifier = Modifier.size(8.dp))

        ImageSongContent(
            modifier = Modifier,
            song = song
        )

        ProgressBarContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            progress = states?.progress ?: 0f,
            currentPosition = states?.currentPosition ?: 0L,
            duration = states?.duration ?: 0L,
            onSeekTo = { progress ->
                viewModel?.processIntent(PlayerMusicIntent.OnSeekTo(progress))
            }
        )

        ControlPlayer(
            modifier = Modifier,
            state = states ?: PlayerMusicState(),
            onPlayPauseClick = {
                viewModel?.processIntent(PlayerMusicIntent.OnPlayPauseClick)
            }
        )
    }
}

private val dummySong = Song(
    id = 1,
    name = "Song Name",
    artist = "Artist Name",
    duration = "01:30",
    image = null,
    uri = null
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewPlayerContent() {
    PlayerMusicContent(
        modifier = Modifier,
        onNavigationIconClick = {},
        song = dummySong
    )
}