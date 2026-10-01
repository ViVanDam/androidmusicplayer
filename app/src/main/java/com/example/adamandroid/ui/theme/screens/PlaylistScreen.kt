package com.example.adamandroid.ui.theme.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adamandroid.ui.theme.components.SongItem
import com.example.adamandroid.ui.theme.components.SongListType
import com.example.adamandroid.viewmodel.MusicPlayerViewModel

@Composable
fun PlaylistScreen(
    viewModel: MusicPlayerViewModel,
    onSongClick: (Int) -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()
    val songs = uiState.songs
    val playlistIds = uiState.playlistSongIds

    val playlist = playlistIds.mapNotNull { id ->
        songs.find { song ->
            song.id == id
        }
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
                text = "My Playlist",
                modifier = Modifier.weight(1f),
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "⚙",
                color = Color.White,
                fontSize = 28.sp
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 10.dp
                ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(
                items = playlist,
                key = { song -> song.id }
            ) { song ->

                SongItem(
                    song = song,
                    isFavorite = song.id in uiState.favoriteSongIds,
                    screenType = SongListType.PLAYLIST,
                    isInPlaylist = song.id in uiState.playlistSongIds,
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
        }

    }
}

//@Preview(
//    showBackground = true,
//    showSystemUi = true
//)
//
//@SuppressLint("ViewModelConstructorInComposable")
//
//@Composable
//fun PlaylistScreenPreview() {
//    val viewModel = MusicPlayerViewModel()
//    PlaylistScreen(
//        viewModel = viewModel,
//        onSongClick = {},
//    )
//}