package com.example.adamandroid.data


data class ScannedSong(
    val id: Long,
    val name: String,
    val artist: String,
    val uri: String,
    val duration: Long
)