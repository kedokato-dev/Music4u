package com.kedokato.music4u.presentation.player.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.compose.getCurrentColorScheme
import com.kedokato.music4u.R
import com.kedokato.music4u.domain.model.Song

@Composable
fun ImageSongContent(modifier: Modifier = Modifier, song: Song) {
    Column(
        modifier = modifier
    ) {
        AsyncImage(
            model = song.image?.toUri(),
            contentDescription = "Image of ${song.name}",
            placeholder = painterResource(id = R.drawable.apple_music),
            error = painterResource(id = R.drawable.apple_music),
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = song.name,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineMedium,
            color = getCurrentColorScheme().onBackground
        )
        Text(
            text = song.artist,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyLarge,
            color = getCurrentColorScheme().onBackground.copy(alpha = 0.7f)
        )

    }
}


@Composable
fun ImageSongContent2(modifier: Modifier = Modifier, song: Song) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_extra),
            contentDescription = "Image of ${song.name}",
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = song.name,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineMedium,
            color = getCurrentColorScheme().onBackground
        )
        Text(
            text = song.artist,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyLarge,
            color = getCurrentColorScheme().onBackground.copy(alpha = 0.7f)
        )
    }
}


@Preview
@Composable
private fun ImageSongContentPreview() {
    ImageSongContent2(
        modifier = Modifier,
        song = Song(
            id = 1,
            name = "Song Name",
            artist = "Artist Name",
            duration = "01:30",
            image = null,
            uri = null
        )
    )
}