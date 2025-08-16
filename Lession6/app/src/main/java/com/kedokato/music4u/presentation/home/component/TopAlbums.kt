package com.kedokato.music4u.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.compose.getCurrentColorScheme
import com.kedokato.music4u.R
import com.kedokato.music4u.domain.model.AlbumInfo
import com.kedokato.music4u.presentation.home.HomeState

@Composable
fun TopAlbums(modifier: Modifier = Modifier, state: HomeState) {

}


@Composable
fun TopAlbumsContent(
    modifier: Modifier = Modifier,
    homeState: HomeState
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 600.dp)
            .background(getCurrentColorScheme().background),
        userScrollEnabled = false // tắt scroll
    ) {
        items(minOf(6, homeState.albums.size)) { index ->
            AlbumItem(
                titleAlbum = homeState.albums[index].name,
                artistName = homeState.albums[index].artist,
                imageUrl = homeState.albums[index].imageUrl
            )
        }
    }
}


@Preview
@Composable
private fun TopAlbumsContentPreview() {
    TopAlbumsContent(
        modifier = Modifier,
        homeState = HomeState(
            albums = listOf(
                // Sample data for preview
                AlbumInfo("Album 1", "Artist 1", "https://lastfm.freetls.fastly.net/i/u/174s/d642cafdcf05a7377c1d52850ec99638.png"),
                AlbumInfo("Album 2", "Artist 2", "https://example.com/image2.jpg"),
                AlbumInfo("Album 3", "Artist 3", "https://example.com/image3.jpg"),
                AlbumInfo("Album 4", "Artist 4", "https://example.com/image4.jpg"),
                AlbumInfo("Album 5", "Artist 5", "https://example.com/image5.jpg"),
                AlbumInfo("Album 6", "Artist 6", "https://example.com/image6.jpg")
            )
        )
    )
}

@Composable
fun AlbumItem(modifier: Modifier = Modifier,
              titleAlbum: String,
              artistName: String,
              imageUrl: String? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(
                getCurrentColorScheme().surfaceTint.copy(alpha = 0.3f),
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))

    ) {

        AsyncImage(
            model = imageUrl,
            placeholder = painterResource(id = R.drawable.img_extra),
            error = painterResource(id = R.drawable.img_extra),
            contentDescription = "Album Cover",
            modifier = modifier
                .size(50.dp)
                .background(getCurrentColorScheme().surface, RoundedCornerShape(12.dp))
        )

        Spacer(modifier = modifier.size(8.dp))

        Column {
            Text(
                text = titleAlbum,
                color = getCurrentColorScheme().onBackground,
                style = MaterialTheme.typography.bodyLarge,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = artistName,
                color = getCurrentColorScheme().onBackground,
                style = MaterialTheme.typography.bodySmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = modifier.padding(top= 4.dp)
            )
        }
    }
}

@Composable
fun AlbumItemForList(
    modifier: Modifier = Modifier,
    titleAlbum: String,
    artistName: String,
    imageUrl: String? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(
                getCurrentColorScheme().surfaceTint.copy(alpha = 0.3f),
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        AsyncImage(
            model = imageUrl,
            placeholder = painterResource(id = R.drawable.img_extra),
            error = painterResource(id = R.drawable.img_extra),
            contentDescription = "Album Cover",
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titleAlbum,
                color = getCurrentColorScheme().onBackground,
                style = MaterialTheme.typography.bodyLarge,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = artistName,
                color = getCurrentColorScheme().onBackground,
                style = MaterialTheme.typography.bodySmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//private fun TopAlbumsPreview() {
//    TopAlbums(
//        modifier = Modifier
//    )
//}