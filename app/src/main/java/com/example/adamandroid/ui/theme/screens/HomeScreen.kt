package com.example.adamandroid.ui.theme.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adamandroid.ui.theme.components.MiniPlayer
import com.example.adamandroid.ui.theme.components.MusicMenuItem
import com.example.adamandroid.ui.theme.components.SongItem
import com.example.adamandroid.ui.theme.components.SongListType
import com.example.adamandroid.viewmodel.MusicPlayerViewModel

// =========================
// HOME SCREEN
// =========================

@Composable
fun HomeScreen(
    viewModel: MusicPlayerViewModel,
    onFavoritesClick: () -> Unit,
    onPlaylistClick: () -> Unit,
    onSongClick: (Int) -> Unit,
    onScanSongClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val songs = uiState.songs

    val filteredSongs = songs.filter {

        it.name.contains(
            uiState.searchText,
            ignoreCase = true
        ) ||
                it.artist.contains(
                    uiState.searchText,
                    ignoreCase = true
                )
    }

//    val currentSong =
//        songs[uiState.currentSongIndex.firstOrNull()]

    val currentSong = uiState.currentSongId?.let { id ->
        uiState.songs.find { it.id == id }
    }

    var expanded by remember {
        mutableStateOf(false)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xFF0D111C)
            )
    ) {

        // =========================
        // HEADER
        // =========================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 50.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "My Music",
                modifier = Modifier.weight(1f),
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Box {
                IconButton(
                    onClick = {
                        expanded = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
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
                    DropdownMenuItem(
                        text = {
                            Text("Quét nhạc trên thiết bị")
                        },
                        onClick = {
                            expanded = false
                            onScanSongClick()
                        }
                    )
                }
            }


        }

        // =========================
        // SEARCH
        // =========================

        OutlinedTextField(
            value = uiState.searchText,
            onValueChange = {
                viewModel.onSearchChange(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            placeholder = {
                Text(
                    text = "🔍 Search",
                    color = Color(0xFF9CA3C7)
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(30.dp)
        )

        // =========================
        // MAIN CONTENT + PLAYER
        // =========================

        Box(
            modifier = Modifier.weight(1f)
        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = 20.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                // Favorites
                item {

                    MusicMenuItem(
                        icon = "❤️",
                        title = "Favorites",
                        onClick = {
                            onFavoritesClick()
                        }
                    )
                }

                // Playlist
                item {

                    MusicMenuItem(
                        icon = "📁",
                        title = "My Playlist",
                        onClick = {
                            onPlaylistClick()
                        }
                    )
                }

                // Section title
                item {

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )

                    Text(
                        text = "Recently Played",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(filteredSongs) { song ->

                    SongItem(
                        song = song,
                        screenType = SongListType.HOME,
                        isInPlaylist = song.id in uiState.playlistSongIds,
                        isFavorite = if(song.id in uiState.favoriteSongIds) {
                            true
                        }else {
                            false
                        },
                        onClick = {
                            val index = song.id - 1
                            viewModel.playSongById(index)
                            onSongClick(song.id)
                        },
                        toggleFavorite = {
                            viewModel.toggleFavorite(song.id)
                        },
                        onAddToPlaylist = {
                            viewModel.addToPlaylist(song.id)
                        },
                        onRemoveFromPlaylist = {
                            viewModel.removeFromPlaylist(song.id)
                        },
                        onDeleteSong = {
                            viewModel.deleteSong(song.id)
                        }

                    )
                }

                // Khoảng trống phía cuối
                item {

                    Spacer(
                        modifier = Modifier.height(100.dp)
                    )
                }
            }
        }

        // =========================
        // MINI PLAYER
        // =========================
        if (uiState.currentSongId != null) {

            val currentSong = uiState.songs.getOrNull(
                uiState.currentSongIndex
            )

            if (currentSong != null) {
                MiniPlayer(
                    song = currentSong,
                    viewModel = viewModel,
                    isPlaying = uiState.isPlaying,
                    isFavorite =
                        currentSong.id in uiState.favoriteSongIds,

                    onPlayPause = {
                        viewModel.togglePlayPause()
                    },

                    onPrevious = {
                        viewModel.previousSong()
                    },

                    onNext = {
                        viewModel.nextSong()
                    },
                    toggleFavorite = {
                        viewModel.toggleFavorite(songId = currentSong.id)
                    },
                    onClick = {
                        onSongClick(currentSong.id)
                    }
                )
            }
        }
    }
}



