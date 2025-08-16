package com.kedokato.music4u.presentation.home.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.kedokato.music4u.R
import com.kedokato.music4u.presentation.home.HomeState

@Composable
fun TopArtist(modifier: Modifier = Modifier) {

}

@Composable
fun TopArtistContent(modifier: Modifier = Modifier, state: HomeState) {
    LazyRow {
        items(minOf(5, state.artists.size)) { index ->
            ArtistItem(
                modifier = modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .size(180.dp),
                artistName = state.artists[index].name,
                artistImageUrl = state.artists[index].imageUrl
            )
        }
    }
}


@Composable
fun ArtistItem(modifier: Modifier = Modifier,
                artistName: String = "Artist Name",
                artistImageUrl: String? = null
)
{
    Box{
        AsyncImage(
            model = artistImageUrl,
            contentDescription = "Artist Image",
            modifier = modifier.size(180.dp),
            placeholder = painterResource(id = R.drawable.catset_img),
            error = painterResource(id = R.drawable.apple_music)
        )

        Text(
            text = artistName,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 8.dp),
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TopArtistPreview(modifier: Modifier = Modifier) {
    TopArtist(modifier = modifier)
}

