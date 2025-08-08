package com.kedokato.lession6.presentation.player.component

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.getCurrentColorScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressBarContent(
    modifier: Modifier = Modifier,
    onValueChangeFinished: (Float) -> Unit = {}
) {
    var sliderPosition by remember { mutableFloatStateOf(89f) }
    var isDragging by remember { mutableStateOf(false) }
    var valueRange by remember { mutableStateOf(0f..180f) }
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Slider(
            value = sliderPosition,
            onValueChange = {
                sliderPosition = it
                isDragging = true
            },
            valueRange = valueRange,
            onValueChangeFinished = {
                onValueChangeFinished(sliderPosition)
                isDragging = false
            },
            colors = SliderDefaults.colors(
                thumbColor = Color.Transparent,
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
                val progress = sliderState.value / sliderState.valueRange.endInclusive

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
                            .fillMaxWidth(progress)
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
                text = "00:00",
                style = MaterialTheme.typography.bodyMedium,
                color = getCurrentColorScheme().onBackground
            )
            Text(
                text = "3:00",
                style = MaterialTheme.typography.bodyMedium,
                color = getCurrentColorScheme().onBackground
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun ProgressBarContentPreview() {
    ProgressBarContent(
        modifier = Modifier.fillMaxWidth(),
        onValueChangeFinished = {}
    )
}