package com.example.adamandroid.ui.theme.screens

import android.annotation.SuppressLint
import android.view.Gravity
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.Player
import com.example.adamandroid.player.MusicPlayerManager
import com.example.adamandroid.ui.theme.components.MusicDisk
import com.example.adamandroid.viewmodel.MusicPlayerViewModel
import kotlin.Int
import com.example.adamandroid.ui.theme.components.MusicProgressBar
import com.example.adamandroid.ui.theme.components.SleepTimerIcon


@Composable
fun PlayerScreen(
    songId: Int,
    viewModel: MusicPlayerViewModel,
    onBackClick: () -> Unit
) {
    var sleepTimerMenuExpanded by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(songId) {
        if (!viewModel.isCurrentSong(songId)) {
            viewModel.playSongById(songId)
        }
    }

    val song = uiState.songs.getOrNull(
        uiState.currentSongIndex
    )

    val isFavorite = song?.id in uiState.favoriteSongIds

    if (song == null) {
        Text(
            text = "Song not found",
            color = Color.White
        )
        return
    }

    val sleepTimerOptions = listOf(
        1,
        30,
        45,
        60,
        100
    )

    val rotation = remember {
        Animatable(0f)
    }

    LaunchedEffect(uiState.isPlaying) {

        if (uiState.isPlaying) {

            while (true) {
                rotation.animateTo(
                    targetValue = rotation.value + 360f,
                    animationSpec = tween(
                        durationMillis = 5000,
                        easing = LinearEasing
                    )
                )
            }

        }
    }


    //giao diện
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFF0D111C)
            )
            .padding(
                horizontal = 20.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // =========================
        // HEADER
        // =========================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBackClick
            ) {
                Text(
                    text = "←",
                    color = Color.White,
                    fontSize = 30.sp
                )
            }

            Text(
                text = "Now Playing",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            IconButton(
                onClick = {
                    // Settings
                }
            ) {
                Text(
                    text = "⚙",
                    color = Color.White,
                    fontSize = 25.sp
                )
            }
        }

        Spacer(
            modifier = Modifier.height(40.dp)
        )

        // =========================
        // MUSIC DISK
        // =========================

        MusicDisk(
            rotation = rotation.value
        )

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        // =========================
        // SONG INFO
        // =========================

        Text(
            text = song.name,
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.height(60.dp)
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = song.artist,
            color = Color(0xFF9CA3C7),
            fontSize = 17.sp
        )

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        MusicProgressBar(
            currentPosition = uiState.currentPosition,
            duration = uiState.duration,
            onSeek = { position ->
                viewModel.seekTo(position)
            }
        )

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        // =========================
        // CONTROLS
        // =========================

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = {
                    viewModel.previousSong()
                }
            ) {
                Text(
                    text = "⏮",
                    fontSize = 32.sp,
                    color = Color.White
                )
            }

//            IconButton(
//                modifier = Modifier
//                    .size(70.dp)
//                    .clip(CircleShape),
//                onClick = {
//                    viewModel.togglePlayPause()
//                }
//            ) {
//                Text(
//                    text = if (uiState.isPlaying) {
//                        "⏸"
//                    } else {
//                        "▶"
//                    },
//                    fontSize = 38.sp,
//                    color = Color.White
//                )
//            }

            IconButton(
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape),
                onClick = {
                    viewModel.togglePlayPause()
                }
            ) {
                Box(
                    modifier = Modifier.size(70.dp),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.animation.AnimatedContent(
                        targetState = uiState.isPlaying,
                        label = "play_pause"
                    ) { isPlaying ->

                        Text(
                            text = if (isPlaying) {
                                "⏸"
                            } else {
                                "▶"
                            },
                            fontSize = 38.sp,
                            color = Color.White
                        )
                    }
                }
            }

            IconButton(
                onClick = {
                    viewModel.nextSong()
                }
            ) {
                Text(
                    text = "⏭",
                    fontSize = 32.sp,
                    color = Color.White
                )
            }
        }

        Spacer(
            modifier = Modifier.height(25.dp)
        )

        // =========================
        // EXTRA CONTROLS
        // =========================

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = {
                    viewModel.toggleShuffle()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Shuffle,
                    contentDescription = "Shuffle",
                    tint = if (uiState.isShuffle) {
                        Color.White
                    } else {
                        Color.Gray
                    },
                    modifier = Modifier
                        .background(
                            color = if (uiState.isShuffle) {
                                Color(0xFF2196F3)
                            } else {
                                Color.Transparent
                            },
                            shape = CircleShape
                        )
                        .padding(8.dp)
                )
            }

            IconButton(
                onClick = {
                    viewModel.toggleRepeat()
                }
            ) {

                Box(
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Repeat,
                        contentDescription = "Repeat",
                        tint = if (
                            uiState.repeatMode == Player.REPEAT_MODE_OFF
                        ) {
                            Color.Gray
                        } else {
                            Color.White
                        },
                        modifier = Modifier
                            .background(
                                color = if (
                                    uiState.repeatMode == Player.REPEAT_MODE_OFF
                                ) {
                                    Color.Transparent
                                } else {
                                    Color(0xFF2196F3)
                                },
                                shape = CircleShape
                            )
                            .padding(8.dp)
                    )

                    // Hiển thị số 1 khi Repeat One
                    if (
                        uiState.repeatMode == Player.REPEAT_MODE_ONE
                    ) {
                        Text(
                            text = "1",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(
                                    x = (-2).dp,
                                    y = 2.dp
                                )
                        )
                    }
                }
            }

            Box {

                IconButton(
                    onClick = {
                        sleepTimerMenuExpanded = true
                    }
                ) {
                    SleepTimerIcon(
                        remainingMinutes =
                            uiState.sleepTimerRemaining?.let {
                                (it / 60_000L).toInt()
                            },
                        onClick = {
                            sleepTimerMenuExpanded = true
                        }
                    )
                }

                DropdownMenu(
                    expanded = sleepTimerMenuExpanded,
                    onDismissRequest = {
                        sleepTimerMenuExpanded = false
                    },
                    modifier = Modifier
                        .background(
                            Color(0xFF1A1F2E),
                            RoundedCornerShape(16.dp)
                        )
                        .width(180.dp)
                ) {

                    Text(
                        text = "Sleep Timer",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(
                            horizontal = 16.dp,
                            vertical = 12.dp
                        )
                    )

                    sleepTimerOptions.forEach { minutes ->

                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "$minutes phút",
                                    color = Color.White,
                                    fontSize = 15.sp
                                )
                            },
                            leadingIcon = {
                                Text(
                                    text = "⏱",
                                    fontSize = 18.sp
                                )
                            },
                            onClick = {
                                sleepTimerMenuExpanded = false
                                viewModel.setSleepTimer(minutes)

                                Toast.makeText(
                                    context,
                                    "Nhạc sẽ dừng sau $minutes phút",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            modifier = Modifier.padding(horizontal = 6.dp)
                        )
                    }
                }
            }

            Icon(
                imageVector = if (song.isFavorite) {
                    Icons.Filled.Favorite
                } else {
                    Icons.Outlined.FavoriteBorder
                },
                contentDescription = "Favorite",
                tint = if (song.isFavorite) {
                    Color.Red
                } else {
                    Color.White
                },
                modifier = Modifier
                    .size(28.dp)
                    .clickable {
                        viewModel.toggleFavorite(song.id)
                    }
            )
        }
    }
}


//@Preview(
//    showBackground = true,
//    showSystemUi = true
//)
//@SuppressLint("ViewModelConstructorInComposable")
//@Composable
//fun PlayerScreenPreview() {
//
//    val viewModel = MusicPlayerViewModel()
//
//    PlayerScreen(
//        viewModel = viewModel,
//        songId = 1,
//        onBackClick = {},
//    )
//}