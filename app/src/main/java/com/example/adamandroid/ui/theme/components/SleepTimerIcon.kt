package com.example.adamandroid.ui.theme.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun SleepTimerIcon(
    remainingMinutes: Int?,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.wrapContentSize()
    ) {

        IconButton(
            onClick = onClick
        ) {
            Icon(
                imageVector = Icons.Default.Timer,
                contentDescription = "Sleep timer",
                tint = Color(0xFF64B5F6)
            )
        }

        if (remainingMinutes != null && remainingMinutes > 0) {
            Text(
                text = "${remainingMinutes}'",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFC107),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(
                        x = (-2).dp,
                        y = (0).dp
                    )
            )
        }
    }
}