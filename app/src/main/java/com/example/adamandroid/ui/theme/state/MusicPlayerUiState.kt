package com.example.adamandroid.ui.theme.state

import androidx.media3.common.Player
import com.example.adamandroid.R
import com.example.adamandroid.data.Song
data class MusicPlayerUiState(

    val songs: List<Song> = emptyList(),
    val searchText: String = "",
    val currentSongIndex: Int = 0,
    val isPlaying: Boolean = false,
    val favoriteSongIds: Set<Int> = setOf(1, 4, 5),
    val playlistSongIds: Set<Int> = setOf(1, 3),
    val currentPosition: Long = 0L,
    val duration: Long = 0L,
//    val isRepeat: Boolean = false,
    val isShuffle: Boolean = false,
    val currentSongId: Int? = null,
    val sleepTimerRemaining: Long? = null,
    val repeatMode: Int = Player.REPEAT_MODE_OFF

)