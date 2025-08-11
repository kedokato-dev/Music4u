package com.kedokato.lession6.presentation.profile.component

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.kedokato.lession6.R


@Composable
fun AvatarImage(
    modifier: Modifier,
    colorScheme: ColorScheme,
    imageUri: Uri? = null,
    onClickCamera: () -> Unit = {},
    isEditMode: Boolean = false
) {
    Box() {
        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                placeholder = painterResource(id = R.drawable.avater_default),
                error = painterResource(id = R.drawable.img4),
                contentDescription = "Avatar Image",
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .size(150.dp)
                    .border(
                        width = 2.dp,
                        color = colorScheme.primary,
                        shape = RoundedCornerShape(150.dp)
                    )
                    .clip(RoundedCornerShape(150.dp))
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.avater_default),
                contentDescription = "Avatar Image",
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .size(150.dp)
                    .border(
                        width = 2.dp,
                        color = colorScheme.primary,
                        shape = RoundedCornerShape(150.dp)
                    )
                    .clip(RoundedCornerShape(150.dp))
            )
        }

        if(isEditMode){
            Box(
                modifier = modifier
                    .align(Alignment.BottomEnd)
                    .padding(2.dp)
                    .background(
                        color = colorScheme.background,
                        shape = RoundedCornerShape(20.dp)
                    )
            ) {
                IconButton(
                    onClick = onClickCamera,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.camera),
                        contentDescription = "Camera Icon",
                        modifier = modifier
                            .size(40.dp)
                            .align(Alignment.CenterEnd)
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(20.dp)
                            ),
                        tint = colorScheme.primary
                    )
                }
            }
        }
    }
}