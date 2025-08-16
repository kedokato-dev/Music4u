package com.kedokato.music4u.presentation.player.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.compose.getCurrentColorScheme
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressBarContent(
    modifier: Modifier = Modifier,
    progress: Float = 0f,
    currentPosition: Long = 0L,
    duration: Long = 0L,
    onSeekTo: (Float) -> Unit = {}
) {
    var isUserSeeking by remember { mutableStateOf(false) }
    var seekProgress by remember { mutableFloatStateOf(0f) }

    // Reset seek state khi không kéo
    val displayProgress = if (isUserSeeking) seekProgress else progress
    val displayPosition = if (isUserSeeking) {
        (seekProgress * duration).toLong()
    } else {
        currentPosition
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Slider(
            value = displayProgress,
            onValueChange = { newValue ->
                isUserSeeking = true
                seekProgress = newValue
            },
            valueRange = 0f..1f,
            onValueChangeFinished = {
                isUserSeeking = false
                onSeekTo(seekProgress)
            },
            colors = SliderDefaults.colors(
                thumbColor = getCurrentColorScheme().primary,
                activeTrackColor = getCurrentColorScheme().primary,
                inactiveTrackColor = getCurrentColorScheme().onBackground.copy(0.5f)
            ),
            thumb = {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            getCurrentColorScheme().primary,
                            CircleShape
                        )
                )
            },
            track = { sliderState ->
                val trackProgress = sliderState.value / sliderState.valueRange.endInclusive

                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                ) {
                    Box(
                        Modifier
                            .matchParentSize()
                            .background(getCurrentColorScheme().onBackground.copy(alpha = 0.5f), CircleShape)
                    )

                    Box(
                        Modifier
                            .fillMaxWidth(trackProgress)
                            .height(8.dp)
                            .background(getCurrentColorScheme().primary, CircleShape)
                    )
                }
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = formatDuration(displayPosition),
                style = MaterialTheme.typography.bodyMedium,
                color = getCurrentColorScheme().onBackground
            )
            Text(
                text = formatDuration(duration),
                style = MaterialTheme.typography.bodyMedium,
                color = getCurrentColorScheme().onBackground
            )
        }
    }
}

fun formatDuration(millis: Long): String {
    val seconds = (millis / 1000) % 60
    val minutes = (millis / (1000 * 60)) % 60
    return String.format("%02d:%02d", minutes, seconds)
}

//
//@Preview(showBackground = true)
//@Composable
//private fun ProgressBarContentPreview() {
//    ProgressBarContent(
//        modifier = Modifier.fillMaxWidth(),
//        onValueChangeFinished = {},
//        progress = 0.5f,
//        currentPosition = 60000L, // 1 phút
//        duration = 180000L // 3 phút
//
//    )
//}