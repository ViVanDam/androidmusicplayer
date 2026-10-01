package com.example.adamandroid.ui.theme.scanmusic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.adamandroid.data.ScannedSong
import com.example.adamandroid.utils.MusicScanner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScanMusicViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val _songs = MutableStateFlow<List<ScannedSong>>(emptyList())
    val songs = _songs.asStateFlow()

    fun scanMusic() {
        viewModelScope.launch {
            val scannedSongs = MusicScanner.scanMusic(
                getApplication<Application>()
            )

            _songs.value = scannedSongs
        }
    }
}