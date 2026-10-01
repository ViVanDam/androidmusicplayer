package com.example.adamandroid.ui.theme.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adamandroid.data.ScannedSong

@Composable
fun ScannedSongItem(
    song: ScannedSong,
    selected: Boolean,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(vertical = 10.dp),
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Checkbox(
            checked = selected,
            onCheckedChange = {
                onClick()
            }
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = song.name,
                color = Color.White,
                fontSize = 17.sp,
                maxLines = 1,
                overflow =
                    TextOverflow.Ellipsis
            )

            Text(
                text = song.artist,
                color = Color.Gray,
                fontSize = 14.sp,
                maxLines = 1,
                overflow =
                    TextOverflow.Ellipsis
            )
        }
    }
}