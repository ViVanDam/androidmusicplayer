package com.example.adamandroid.data

fun SongEntity.toSong(): Song {
    return Song(
        id = id,
        name = name,
        artist = artist,
        uri = uri,
        duration = duration,
        isFavorite = isFavorite,
        isInPlaylist = isInPlaylist
    )
}

fun Song.toSongEntity(
    isFavorite: Boolean = false,
    isInPlaylist: Boolean = false
): SongEntity {
    return SongEntity(
        id = id,
        name = name,
        artist = artist,
        uri = uri,
        duration = duration,
        isFavorite = isFavorite,
        isInPlaylist = isInPlaylist
    )
}