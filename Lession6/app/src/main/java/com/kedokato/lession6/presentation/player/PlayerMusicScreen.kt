package com.kedokato.lession6.presentation.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.getCurrentColorScheme
import com.kedokato.lession6.domain.model.Song
import com.kedokato.lession6.presentation.player.component.ControlPlayer
import com.kedokato.lession6.presentation.player.component.ImageSongContent2
import com.kedokato.lession6.presentation.player.component.ProgressBarContent

@Composable
fun PlayerMusicScreen(
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {},
    song: Song) {


    PlayerMusicScreen(
        modifier = Modifier,
        onNavigationIconClick = onNavigationIconClick,
        song = song
    )
}


@Composable
fun PlayerMusicContent(
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {},
    song: Song
) {

    Column(
        modifier = modifier
            .background(getCurrentColorScheme().background)
            .fillMaxSize()
            .padding(32.dp)
    )
    {
        Spacer(
            modifier = Modifier.size(8.dp)
        )

        ImageSongContent2(
            modifier = Modifier,
            song = song
        )

        ProgressBarContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            onValueChangeFinished = {}
        )

        ControlPlayer(
            modifier = Modifier
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