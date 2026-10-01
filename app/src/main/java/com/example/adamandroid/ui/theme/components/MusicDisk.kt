package com.example.adamandroid.ui.theme.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun MusicDisk(
    rotation: Float
) {
    Canvas(
        modifier = Modifier
            .size(280.dp)
            .graphicsLayer {
                rotationZ = rotation
            }
    ) {

        val center = Offset(
            x = size.width / 2,
            y = size.height / 2
        )

        val radius = size.minDimension / 2

        // =========================
        // VINYL
        // =========================

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF383838),
                    Color(0xFF161616),
                    Color(0xFF050505)
                ),
                center = center,
                radius = radius
            ),
            radius = radius,
            center = center
        )

        // =========================
        // VINYL GROOVES
        // =========================

        for (i in 1..20) {

            val grooveRadius =
                radius * (0.28f + i * 0.033f)

            drawCircle(
                color = Color(0xFF252525),
                radius = grooveRadius,
                center = center,
                style = Stroke(
                    width = 1.1f
                )
            )
        }

        // =========================
        // RAINBOW LIGHT
        // =========================

        val rainbowBrush = Brush.sweepGradient(
            colors = listOf(
                Color(0xFFFF0000),
                Color(0xFFFF7A00),
                Color(0xFFFFFF00),
                Color(0xFF00FF66),
                Color(0xFF00CCFF),
                Color(0xFF3366FF),
                Color(0xFFAA00FF),
                Color(0xFFFF00AA),
                Color(0xFFFF0000)
            ),
            center = center
        )

        // Vòng ánh sáng cầu vồng
        drawCircle(
            brush = rainbowBrush,
            radius = radius * 0.78f,
            center = center,
            style = Stroke(
                width = 5f
            )
        )

        // =========================
        // RAINBOW LIGHT RAYS
        // =========================

        val rayCount = 14

        for (i in 0 until rayCount) {

            val angle =
                Math.toRadians(
                    (360f / rayCount * i).toDouble()
                )

            val startRadius = radius * 0.32f
            val endRadius = radius * 0.88f

            val start = Offset(
                x = center.x +
                        cos(angle).toFloat() * startRadius,
                y = center.y +
                        sin(angle).toFloat() * startRadius
            )

            val end = Offset(
                x = center.x +
                        cos(angle).toFloat() * endRadius,
                y = center.y +
                        sin(angle).toFloat() * endRadius
            )

            drawLine(
                brush = rainbowBrush,
                start = start,
                end = end,
                strokeWidth = 2.5f,
                cap = StrokeCap.Round
            )
        }

        // =========================
        // CENTER LABEL
        // =========================

        drawCircle(
            color = Color(0xFF8B1E3F),
            radius = radius * 0.25f,
            center = center
        )

        drawCircle(
            color = Color(0xFFB8325A),
            radius = radius * 0.18f,
            center = center
        )

        // =========================
        // CENTER HOLE
        // =========================

        drawCircle(
            color = Color(0xFF111111),
            radius = radius * 0.055f,
            center = center
        )

        drawCircle(
            color = Color.LightGray,
            radius = radius * 0.018f,
            center = center
        )
    }
}
