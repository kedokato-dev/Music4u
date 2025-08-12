package com.kedokato.lession6.presentation.player.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.getCurrentColorScheme
import com.kedokato.lession6.R
import com.kedokato.lession6.presentation.player.PlayerMusicState

@Composable
fun ControlPlayer(
    modifier: Modifier = Modifier,
    state: PlayerMusicState,
    onShuffleClick: () -> Unit = {},
    onPreviousClick: () -> Unit = {},
    onPlayPauseClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    onRepeatClick: () -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
          IconControl(
              modifier = modifier,
              icon =R.drawable.shuffle,
              size = 24,
              color = if(state.isShuffleMode)
                  getCurrentColorScheme().primary.hashCode()
              else
                  getCurrentColorScheme().onBackground.hashCode(),
              onClick = {
                  onShuffleClick()
              }

          )


        IconControl(
            modifier = modifier,
            icon = R.drawable.skip_previous,
            size = 48,
            color = getCurrentColorScheme().onBackground.hashCode(),
            onClick = {
                onPreviousClick()
            }
        )

        IconControl(
            modifier = modifier.size(64.dp),
            icon = if (state.isPlaying) R.drawable.pause_circle else R.drawable.play_circle,
            size = 128,
            color = getCurrentColorScheme().primary.hashCode(),
            onClick = {
                if (state.isPlaying) {
                    onPlayPauseClick()
                } else {
                    onPlayPauseClick()
                }
            }
        )

        IconControl(
            modifier = modifier,
            icon = R.drawable.skip_next,
            size = 48,
            color = getCurrentColorScheme().onBackground.hashCode(),
            onClick = {
                onNextClick()
            }
        )

        IconControl(
            modifier = modifier,
            icon =  R.drawable.repeat,
            size = 24,
            color = if(state.isRepeatMode)
                getCurrentColorScheme().primary.hashCode()
            else
                getCurrentColorScheme().onBackground.hashCode(),
            onClick = {
                onRepeatClick()
            }
        )


    }
}

@Composable
fun IconControl(modifier: Modifier = Modifier, icon: Int, size: Int, color: Int, onClick: () -> Unit = {}) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = "Control Icon",
            modifier = Modifier
                .size(size.dp),
            tint = Color(color)
        )
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ControlPlayerPreview() {
    ControlPlayer(
        modifier = Modifier,
        state = PlayerMusicState(isPlaying = true, song = null)
    )
}