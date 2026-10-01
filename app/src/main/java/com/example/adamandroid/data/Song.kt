package com.example.adamandroid.data

import androidx.annotation.RawRes

//data class Song(
//    val id: Int,
//    val name: String,
//    val artist: String,
//    val resourceId: Int
//)

//data class Song(
//    val id: Int,
//    val name: String,
//    val artist: String,
//    val uri: String,
//    val duration: Long = 0L
//)


data class Song(
    val id: Int,
    val name: String,
    val artist: String,
    val uri: String,
    val audioResId: Int? = null,
    val duration: Long = 0L,
    val isFavorite: Boolean = false,
    val isInPlaylist: Boolean = false
)