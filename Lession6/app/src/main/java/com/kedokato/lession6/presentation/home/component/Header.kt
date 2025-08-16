package com.kedokato.lession6.presentation.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.compose.getCurrentColorScheme
import com.kedokato.lession6.R
import com.kedokato.lession6.domain.model.User

@Composable
fun HeaderScreen(modifier: Modifier = Modifier,
                 onNavigationSettings: () -> Unit = {},
                 oNavigationProfile: () -> Unit = {},
                 user: User?) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        AsyncImage(
            model = user?.avatarUri,
            contentDescription = "Header Image",
            placeholder = painterResource(id = R.drawable.avater_default),
            error = painterResource(id = R.drawable.avater_default),
            modifier = modifier
                .size(50.dp)
                .clip(CircleShape)
                .border(color = getCurrentColorScheme().primary, width = 2.dp, shape = CircleShape)
                .clickable { oNavigationProfile() }
        )

        Spacer(modifier.size(8.dp))

        UserInformation(
            modifier = Modifier,
            fullName = user?.name ?: "Unknown User"
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = { onNavigationSettings() },
            modifier = Modifier
                .size(50.dp),
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.setting),
                    contentDescription = "Profile Icon",
                    modifier = Modifier.size(32.dp),
                    tint = getCurrentColorScheme().onBackground,
                )
            }
        )
    }
}


@Composable
fun UserInformation(modifier: Modifier = Modifier, fullName: String = "Kedokato") {
    Column(
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = stringResource(R.string.wellcome),
            modifier = modifier,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = getCurrentColorScheme().onBackground
        )

        Text(
            text = fullName,
            modifier = modifier,
            style = MaterialTheme.typography.bodyMedium,
            color = getCurrentColorScheme().onBackground.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun Subtitle(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy( space = 2.dp,Alignment.Start),
    ) {
        Image(
            painter = painterResource(id = R.drawable.king),
            contentDescription = "Subtitle Icon",
            modifier = modifier
                .size(34.dp)
                .then(modifier)
        )

        Text(
            text = stringResource(R.string.rank),
            modifier = modifier,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = getCurrentColorScheme().primary
        )
    }
}


//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//private fun HeaderScreen() {
//    HeaderScreen(modifier = Modifier)
//}