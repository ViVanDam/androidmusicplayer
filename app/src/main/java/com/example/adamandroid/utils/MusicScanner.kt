package com.example.adamandroid.utils

import android.content.Context
import android.provider.MediaStore
import com.example.adamandroid.data.ScannedSong

object MusicScanner{

    fun scanMusic(context: Context): List<ScannedSong> {

        val songs = mutableListOf<ScannedSong>()

        val collection =
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION
        )

        val selection =
            "${MediaStore.Audio.Media.IS_MUSIC} != 0"

        val sortOrder =
            "${MediaStore.Audio.Media.TITLE} ASC"

        context.contentResolver.query(
            collection,
            projection,
            selection,
            null,
            sortOrder
        )?.use { cursor ->

            val idColumn =
                cursor.getColumnIndexOrThrow(
                    MediaStore.Audio.Media._ID
                )

            val titleColumn =
                cursor.getColumnIndexOrThrow(
                    MediaStore.Audio.Media.TITLE
                )

            val artistColumn =
                cursor.getColumnIndexOrThrow(
                    MediaStore.Audio.Media.ARTIST
                )

            val durationColumn =
                cursor.getColumnIndexOrThrow(
                    MediaStore.Audio.Media.DURATION
                )

            while (cursor.moveToNext()) {

                val id =
                    cursor.getLong(idColumn)

                val title =
                    cursor.getString(titleColumn)

                val artist =
                    cursor.getString(artistColumn)

                val duration =
                    cursor.getLong(durationColumn)

                val uri =
                    "${MediaStore.Audio.Media.EXTERNAL_CONTENT_URI}/$id"

                songs.add(
                    ScannedSong(
                        id = id,
                        name = title,
                        artist = artist,
                        uri = uri,
                        duration = duration
                    )
                )
            }
        }

        return songs
    }
}