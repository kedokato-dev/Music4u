package com.kedokato.music4u.presentation.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.getCurrentColorScheme
import com.kedokato.music4u.domain.model.Song
import com.kedokato.music4u.presentation.player.component.ControlPlayer
import com.kedokato.music4u.presentation.player.component.ImageSongContent
import com.kedokato.music4u.presentation.player.component.PlayerTopAppBar
import com.kedokato.music4u.presentation.player.component.ProgressBarContent
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlayerMusicScreen(
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {},
    song: Song
) {
    val playerMusicVM: PlayerMusicViewModel = koinViewModel()
    val playerMusicState = playerMusicVM.playerMusicState.collectAsState().value

    LaunchedEffect(song.id) {
        if (playerMusicState.song?.id != song.id) {
            playerMusicVM.playNewSong(song)
        }
    }


            PlayerMusicContent(
                modifier = Modifier,
                onNavigationIconClick = onNavigationIconClick,
                song = playerMusicState.song ?: song,
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
    LazyColumn(
        modifier = modifier
            .background(getCurrentColorScheme().background)
            .fillMaxSize()
    ) {

        item {
            PlayerTopAppBar(
                modifier = Modifier,
                onNavigationIconClick = onNavigationIconClick,
                onClosePlayer = {
                    viewModel?.processIntent(PlayerMusicIntent.OnStopSong)
                    onNavigationIconClick()
                }
            )
        }

        item{
            ImageSongContent(
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp),
                song = song
            )
        }

        item {
            ProgressBarContent(
                modifier = Modifier
                    .padding(horizontal = 48.dp, vertical = 16.dp)
                    .fillMaxWidth(),
                progress = states?.progress ?: 0f,
                currentPosition = states?.currentPosition ?: 0L,
                duration = states?.duration ?: 0L,
                onSeekTo = { progress ->
                    viewModel?.processIntent(PlayerMusicIntent.OnSeekTo(progress))
                }
            )
        }

        item{
            ControlPlayer(
                modifier = Modifier,
                state = states ?: PlayerMusicState(),
                onShuffleClick = {
                    viewModel?.processIntent(PlayerMusicIntent.OnShuffleClick)
                },
                onPreviousClick = {
                    viewModel?.processIntent(PlayerMusicIntent.OnPreviousClick(song.id))
                },


                onPlayPauseClick = {
                    viewModel?.processIntent(PlayerMusicIntent.OnPlayPauseClick)
                },
                onNextClick = {
                    viewModel?.processIntent(PlayerMusicIntent.OnNextClick(song.id))
                },
                onRepeatClick = {
                    viewModel?.processIntent(PlayerMusicIntent.OnRepeatClick)
                }
            )
        }
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