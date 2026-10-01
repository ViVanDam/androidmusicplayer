package com.example.adamandroid.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.example.adamandroid.R
import com.example.adamandroid.data.MusicDatabase
import com.example.adamandroid.data.ScannedSong
import com.example.adamandroid.data.Song
import com.example.adamandroid.data.SongEntity
import com.example.adamandroid.data.SongRepository
import com.example.adamandroid.data.toSong
import com.example.adamandroid.player.MusicPlayerManager
import com.example.adamandroid.ui.theme.state.MusicPlayerUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MusicPlayerViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database =
        MusicDatabase.getDatabase(application)

    private val repository =
        SongRepository(database.songDao())

    // ========================================
    // MUSIC PLAYER
    // ========================================

    private val musicPlayer =
        MusicPlayerManager(
            application.applicationContext
        )

    // ========================================
    // RAW URI
    // ========================================

    private fun rawUri(resourceId: Int): String {
        return "android.resource://${getApplication<Application>().packageName}/$resourceId"
    }

    // ========================================
    // INITIAL SONGS
    // ========================================

    private var lastPlaylistUris: List<String> = emptyList()

    // ========================================
    // UI STATE
    // ========================================

    private val _uiState =
        MutableStateFlow(
            MusicPlayerUiState()
        )

    val uiState =
        _uiState.asStateFlow()

    // ========================================
    // SLEEP TIMER
    // ========================================

    private var sleepTimerJob: Job? = null

    // ========================================
    // PLAYER LISTENER
    // ========================================

    private val playerListener =
        object : Player.Listener {

            override fun onIsPlayingChanged(
                isPlaying: Boolean
            ) {
                _uiState.update {
                    it.copy(
                        isPlaying = isPlaying
                    )
                }
            }

            override fun onMediaItemTransition(
                mediaItem: MediaItem?,
                reason: Int
            ) {
                if (mediaItem == null) return

                val songId =
                    mediaItem.mediaId.toIntOrNull()
                        ?: return

                val songs =
                    _uiState.value.songs

                val index =
                    songs.indexOfFirst {
                        it.id == songId
                    }

                if (index == -1) return

                _uiState.update {
                    it.copy(
                        currentSongId = songId,
                        currentSongIndex = index,
                        currentPosition = 0L
                    )
                }
            }
        }
    private fun observeSongs() {

        viewModelScope.launch {

            repository.songs.collect { songEntities ->

                val songs =
                    songEntities.map {
                        it.toSong()
                    }

                _uiState.update {
                    it.copy(
                        songs = songs
                    )
                }

                val playlistUris =
                    songs.map {
                        it.uri
                    }

                if (playlistUris != lastPlaylistUris) {

                    lastPlaylistUris =
                        playlistUris

                    musicPlayer.setPlaylist(
                        songs
                    )
                }
            }
        }
    }


    // ========================================
    // INIT
    // ========================================

    init {
        observeSongs()

        musicPlayer.addListener(
            playerListener
        )

        startPositionUpdates()
    }

    // ========================================
    // SEARCH
    // ========================================

    fun onSearchChange(
        value: String
    ) {
        _uiState.update {
            it.copy(
                searchText = value
            )
        }
    }

    private fun generateSongId(
        songs: List<Song>
    ): Int {
        return (songs.maxOfOrNull { it.id } ?: 0) + 1
    }

    fun addScannedSongs(
        scannedSongs: List<ScannedSong>
    ) {

        viewModelScope.launch {

            val newSongs = scannedSongs.map { scannedSong ->

                SongEntity(
                    name = scannedSong.name,
                    artist = scannedSong.artist,
                    uri = scannedSong.uri,
                    duration = scannedSong.duration
                )
            }

            repository.addSongs(newSongs)
        }
    }

    // ========================================
    // PLAY SONG
    // ========================================

    fun playSongById(songId: Int) {

        val state = _uiState.value

        val index = state.songs.indexOfFirst {
            it.id == songId
        }

        if (index == -1) return

        musicPlayer.playAt(index)
    }


    // ========================================
    // PLAY / PAUSE
    // ========================================

    fun togglePlayPause() {

        if (musicPlayer.isPlaying()) {
            musicPlayer.pause()
        } else {
            musicPlayer.play()
        }

        // Không cần update isPlaying thủ công.
        // onIsPlayingChanged() sẽ xử lý.
    }

    // ========================================
    // NEXT
    // ========================================

    fun nextSong() {
        musicPlayer.next()
    }

    // ========================================
    // PREVIOUS
    // ========================================

    fun previousSong() {
        musicPlayer.previous()
    }

    // ========================================
    // SEEK
    // ========================================

    fun seekTo(
        position: Long
    ) {

        musicPlayer.seekTo(
            position
        )

        _uiState.update {
            it.copy(
                currentPosition = position
            )
        }
    }

    // ========================================
    // FAVORITE
    // ========================================
    fun toggleFavorite(songId: Int) {
        viewModelScope.launch {
            val song = _uiState.value.songs
                .find { it.id == songId }
                ?: return@launch

            repository.updateFavorite(
                songId = songId,
                isFavorite = !song.isFavorite
            )
        }
    }

    fun togglePlaylist(songId: Int) {
        viewModelScope.launch {
            val song = _uiState.value.songs
                .find { it.id == songId }
                ?: return@launch

            repository.updatePlaylist(
                songId = songId,
                isInPlaylist = !song.isInPlaylist
            )
        }
    }

    // ========================================
    // PLAYLIST
    // ========================================

    fun addToPlaylist(
        songId: Int
    ) {

        _uiState.update { state ->

            state.copy(
                playlistSongIds =
                    state.playlistSongIds
                        .plus(songId)
            )
        }
    }

    fun removeFromPlaylist(
        songId: Int
    ) {

        _uiState.update { state ->

            state.copy(
                playlistSongIds =
                    state.playlistSongIds
                        .minus(songId)
            )
        }
    }

    // ========================================
    // DELETE SONG
    // ========================================

    fun deleteSong(
        songId: Int
    ) {

        _uiState.update { state ->

            state.copy(

                songs =
                    state.songs.filter {
                        it.id != songId
                    },

                favoriteSongIds =
                    state.favoriteSongIds
                        .minus(songId),

                playlistSongIds =
                    state.playlistSongIds
                        .minus(songId)
            )
        }
    }

    // ========================================
    // ADD SONGS FROM DEVICE
    // ========================================

    fun addSongs(
        songs: List<Song>
    ) {

        if (songs.isEmpty()) return

        _uiState.update { state ->

            val existingUris =
                state.songs
                    .map { it.uri }
                    .toSet()

            val newSongs =
                songs.filter { song ->
                    song.uri !in existingUris
                }

            state.copy(
                songs =
                    state.songs + newSongs
            )
        }
    }

    // ========================================
    // POSITION UPDATE
    // ========================================

    private fun startPositionUpdates() {

        viewModelScope.launch {

            while (isActive) {

                delay(500)

                if (!musicPlayer.isPlaying()) {
                    continue
                }

                val position =
                    musicPlayer.getCurrentPosition()

                val duration =
                    musicPlayer.getDuration()

                _uiState.update {

                    it.copy(
                        currentPosition =
                            position,

                        duration =
                            duration
                    )
                }
            }
        }
    }

    // ========================================
    // CURRENT SONG
    // ========================================

    fun isCurrentSong(
        songId: Int
    ): Boolean {

        return _uiState.value.currentSongId ==
                songId
    }

    // ========================================
    // SHUFFLE
    // ========================================

    fun toggleShuffle() {

        val enabled =
            !_uiState.value.isShuffle

        musicPlayer.setShuffle(
            enabled
        )

        _uiState.update {
            it.copy(
                isShuffle = enabled
            )
        }
    }

    // ========================================
    // REPEAT
    // ========================================

    fun toggleRepeat() {

        val current =
            _uiState.value.repeatMode

        val next =
            when (current) {

                Player.REPEAT_MODE_OFF ->
                    Player.REPEAT_MODE_ALL

                Player.REPEAT_MODE_ALL ->
                    Player.REPEAT_MODE_ONE

                else ->
                    Player.REPEAT_MODE_OFF
            }

        musicPlayer.setRepeatMode(
            next
        )

        _uiState.update {
            it.copy(
                repeatMode = next
            )
        }
    }

    // ========================================
    // SLEEP TIMER
    // ========================================

    fun setSleepTimer(
        minutes: Int
    ) {

        sleepTimerJob?.cancel()

        sleepTimerJob =
            viewModelScope.launch {

                var remaining =
                    minutes * 60_000L

                while (remaining > 0) {

                    _uiState.update {
                        it.copy(
                            sleepTimerRemaining =
                                remaining
                        )
                    }

                    delay(1_000L)

                    remaining -= 1_000L
                }

                musicPlayer.pause()

                _uiState.update {
                    it.copy(
                        isPlaying = false,
                        sleepTimerRemaining =
                            null
                    )
                }

                sleepTimerJob = null
            }
    }

    // ========================================
    // CLEAR
    // ========================================

    override fun onCleared() {

        sleepTimerJob?.cancel()

        musicPlayer.removeListener(
            playerListener
        )

        musicPlayer.release()

        super.onCleared()
    }
}