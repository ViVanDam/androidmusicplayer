package com.example.adamandroid.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.adamandroid.data.Song
import com.example.adamandroid.viewmodel.MusicPlayerViewModel
@Composable
fun MiniPlayer(
    song: Song,
    viewModel: MusicPlayerViewModel,
    isPlaying: Boolean,
    isFavorite: Boolean,
    onPlayPause: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    toggleFavorite: () -> Unit,
    onClick: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFF171C2B)
            )
            .padding(
                horizontal = 16.dp,
                vertical = 14.dp
            )
            .navigationBarsPadding()
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(58.dp)
                    .background(
                        color = Color(0xFF5555E8),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "🎵",
                    fontSize = 28.sp
                )
            }

            Spacer(
                modifier = Modifier.width(14.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable{
                        onClick()
                    }
            ) {

                Text(
                    text = song.name,
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = song.artist,
                    color = Color(0xFF9CA3C7),
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Favorite
            IconButton(
                onClick = {
                    toggleFavorite()
                }
            ) {
                Text(
                    text = if (isFavorite) "❤️" else "♡",
                    fontSize = 24.sp,
                    color = Color.White
                )
            }

            // Previous
            Text(
                text = "⏮",
                color = Color.White,
                fontSize = 24.sp,
                modifier = Modifier.clickable {
                    onPrevious()
                }
            )

            Spacer(
                modifier = Modifier.width(18.dp)
            )

            // Play / Pause
            IconButton(
                onClick = {
                    onPlayPause()
                },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = if (isPlaying) {
                        Icons.Default.Pause
                    } else {
                        Icons.Default.PlayArrow
                    },
                    contentDescription = if (isPlaying) {
                        "Pause"
                    } else {
                        "Play"
                    },
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(
                modifier = Modifier.width(18.dp)
            )

            // Next
            Text(
                text = "⏭",
                color = Color.White,
                fontSize = 24.sp,
                modifier = Modifier.clickable {
                    onNext()
                }

            )
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        MusicProgressBar(
            currentPosition = uiState.currentPosition,
            duration = uiState.duration,
            onSeek = { position ->
                viewModel.seekTo(position)
            }
        )
    }
}