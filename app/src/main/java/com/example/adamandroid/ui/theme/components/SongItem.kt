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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.adamandroid.data.Song


// =========================
// SONG ITEM
// =========================

@Composable
fun SongItem(
    song: Song,
    isFavorite: Boolean,
    onClick: () -> Unit,
    toggleFavorite: () -> Unit,
    onAddToPlaylist: () -> Unit,
    onRemoveFromPlaylist: () -> Unit,
    onDeleteSong: () -> Unit,
    screenType: SongListType,
    isInPlaylist: Boolean
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 5.dp,
                vertical = 10.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        // Album image / music icon
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(
                    color = Color(0xFF252B50),
                    shape = RoundedCornerShape(14.dp)
                ),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "🎵",
                fontSize = 28.sp
            )
        }

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        // Song information
        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = song.name,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = song.artist,
                color = Color(0xFF9CA3C7),
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier.size(32.dp),
                onClick = {
                    toggleFavorite()
                }
            ) {
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
                )
            }

            Spacer(
                modifier = Modifier.width(2.dp)
            )


            Box {

                IconButton(
                    onClick = {
                        expanded = true
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More options",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    when (screenType) {

                        SongListType.HOME -> {
                            if (isInPlaylist) {
                                DropdownMenuItem(
                                    text = {
                                        Text("Xóa khỏi playlist")
                                    },
                                    onClick = {
                                        expanded = false
                                        onRemoveFromPlaylist()
                                    }
                                )

                                DropdownMenuItem(
                                    text = {
                                        Text("Xóa bài hát khỏi ứng dụng")
                                    },
                                    onClick = {
                                        expanded = false
                                        onDeleteSong()
                                    }
                                )

                            } else {
                                DropdownMenuItem(
                                    text = {
                                        Text("Thêm vào playlist")
                                    },
                                    onClick = {
                                        expanded = false
                                        onAddToPlaylist()
                                    }
                                )

                                DropdownMenuItem(
                                    text = {
                                        Text("Xóa bài hát khỏi ứng dụng")
                                    },
                                    onClick = {
                                        expanded = false
                                        onDeleteSong()
                                    }
                                )
                            }
                        }

                        SongListType.PLAYLIST -> {
                            DropdownMenuItem(
                                text = {
                                    Text("Xóa khỏi playlist")
                                },
                                onClick = {
                                    expanded = false
                                    onRemoveFromPlaylist()
                                }
                            )
                        }

                        SongListType.FAVORITE -> {
                            Text("Xóa khỏi playlist")
                        }
                    }
                }
            }
        }
    }
}
