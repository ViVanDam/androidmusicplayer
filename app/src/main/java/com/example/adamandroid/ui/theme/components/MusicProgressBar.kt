package com.example.adamandroid.ui.theme.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.height
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput


private fun formatTime(
    milliseconds: Long
): String {

    val totalSeconds = milliseconds / 1000

    val minutes = totalSeconds / 60

    val seconds = totalSeconds % 60

    return "%02d:%02d".format(
        minutes,
        seconds
    )
}

@Composable
fun MusicProgressBar(
    currentPosition: Long,
    duration: Long,
    onSeek: (Long) -> Unit,
    modifier: Modifier = Modifier
) {

    var sliderPosition by remember {
        mutableFloatStateOf(0f)
    }

    var isDragging by remember {
        mutableStateOf(false)
    }

    val actualPosition =
        if (duration > 0) {
            (
                    currentPosition.toFloat() /
                            duration.toFloat()
                    ).coerceIn(0f, 1f)
        } else {
            0f
        }

    val displayPosition =
        if (isDragging) {
            (sliderPosition * duration).toLong()
        } else {
            currentPosition
        }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .pointerInput(duration) {

                    detectDragGestures(

                        onDragStart = { offset ->

                            isDragging = true

                            sliderPosition =
                                (
                                        offset.x /
                                                size.width
                                        ).coerceIn(
                                        0f,
                                        1f
                                    )
                        },

                        onDrag = { change, _ ->

                            change.consume()

                            sliderPosition =
                                (
                                        change.position.x /
                                                size.width
                                        ).coerceIn(
                                        0f,
                                        1f
                                    )
                        },

                        onDragEnd = {

                            val position =
                                (
                                        sliderPosition *
                                                duration
                                        ).toLong()

                            onSeek(position)

                            isDragging = false
                        },

                        onDragCancel = {

                            isDragging = false
                        }
                    )
                }
        ) {

            val centerY =
                size.height / 2

            val startX = 0f

            val endX =
                size.width

            // Độ dài phần đã phát
            val progressX =
                startX +
                        (
                                endX - startX
                                ) * (
                        if (isDragging) {
                            sliderPosition
                        } else {
                            actualPosition
                        }
                        )

            // Track chưa phát
            drawLine(
                color = Color.White.copy(
                    alpha = 0.25f
                ),
                start = Offset(
                    startX,
                    centerY
                ),
                end = Offset(
                    endX,
                    centerY
                ),
                strokeWidth = 4.dp.toPx()
            )

            // Track đã phát
            drawLine(
                color = Color.White,
                start = Offset(
                    startX,
                    centerY
                ),
                end = Offset(
                    progressX,
                    centerY
                ),
                strokeWidth = 4.dp.toPx()
            )

            // Thumb
            drawCircle(
                color = Color.White,
                radius =
                    if (isDragging) {
                        7.dp.toPx()
                    } else {
                        5.dp.toPx()
                    },
                center = Offset(
                    progressX,
                    centerY
                )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp)
        ) {

            Text(
                text = formatTime(
                    displayPosition
                ),
                color = Color.White,
                fontSize = 12.sp
            )

            Spacer(
                modifier = Modifier.weight(1f)
            )

            Text(
                text = formatTime(duration),
                color = Color.White.copy(
                    alpha = 0.5f
                ),
                fontSize = 12.sp
            )
        }
    }
}