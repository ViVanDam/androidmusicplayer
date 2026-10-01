package com.example.adamandroid.data

import kotlinx.coroutines.flow.Flow

class SongRepository(
    private val songDao: SongDao
) {

    val songs: Flow<List<SongEntity>> =
        songDao.getAllSongs()

    suspend fun addSongs(
        songs: List<SongEntity>
    ) {
        songDao.insertSongs(songs)
    }

    suspend fun addSong(
        song: SongEntity
    ) {
        songDao.insertSong(song)
    }

    suspend fun updateSong(
        song: SongEntity
    ) {
        songDao.updateSong(song)
    }

    suspend fun deleteSong(
        song: SongEntity
    ) {
        songDao.deleteSong(song)
    }

    suspend fun deleteAllSongs() {
        songDao.deleteAllSongs()
    }

    suspend fun getSongByUri(
        uri: String
    ): SongEntity? {
        return songDao.getSongByUri(uri)
    }

    suspend fun updateFavorite(
        songId: Int,
        isFavorite: Boolean
    ) {
        songDao.updateFavorite(songId, isFavorite)
    }

    suspend fun updatePlaylist(
        songId: Int,
        isInPlaylist: Boolean
    ) {
        songDao.updatePlaylist(songId, isInPlaylist)
    }
}