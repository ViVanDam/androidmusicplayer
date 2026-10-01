package com.example.adamandroid.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "songs",
    indices = [
        Index(value = ["uri"], unique = true)
    ]
)
data class SongEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,

    val artist: String,

    val uri: String,

    val duration: Long,

    val isFavorite: Boolean = false,

    val isInPlaylist: Boolean = false
)