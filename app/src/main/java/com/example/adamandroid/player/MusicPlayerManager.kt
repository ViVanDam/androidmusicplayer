package com.example.adamandroid.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.example.adamandroid.data.Song

class MusicPlayerManager(
    private val context: Context
) {

    private val musicController =
        MusicController(context)

//    private var controller: Player? = null
    private var controller: MediaController? = null

    private var pendingSongs: List<Song>? = null
    private val listeners =
        mutableListOf<Player.Listener>()

    init {

        musicController.controllerFuture.addListener(
            {

                controller =
                    musicController.controllerFuture.get()

                listeners.forEach { listener ->
                    controller?.addListener(listener)
                }

                // Controller đã sẵn sàng
                // Nếu trước đó Room đã gửi playlist
                // thì đưa playlist vào Media3 ngay bây giờ.
                pendingSongs?.let { songs ->

                    setPlaylistInternal(songs)

                    pendingSongs = null
                }

            },
            { runnable ->
                runnable.run()
            }
        )
    }

    fun addListener(listener: Player.Listener) {

        listeners.add(listener)

        // Nếu controller đã kết nối rồi
        // thì thêm listener ngay
        controller?.addListener(listener)
    }

    fun removeListener(listener: Player.Listener) {

        listeners.remove(listener)

        controller?.removeListener(listener)
    }

    // =========================
    // PLAYLIST
    // =========================
    private fun createMediaItem(song: Song): MediaItem {
        return MediaItem.Builder()
            .setMediaId(song.id.toString())
            .setUri(song.uri)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(song.name)
                    .setArtist(song.artist)
                    .build()
            )
            .build()
    }

    private fun setPlaylistInternal(
        songs: List<Song>
    ) {

        val player =
            controller ?: return

        if (songs.isEmpty()) {
            player.clearMediaItems()
            return
        }

        val mediaItems =
            songs.map { song ->
                createMediaItem(song)
            }

        player.setMediaItems(
            mediaItems
        )

        player.prepare()
    }
    fun setPlaylist(songs: List<Song>) {

        // MediaController chưa kết nối
        if (controller == null) {
            pendingSongs = songs
            return
        }

        setPlaylistInternal(songs)
    }

    // =========================
    // PLAYBACK
    // =========================

    fun play() {
        controller?.play()
    }

    fun pause() {
        controller?.pause()
    }

    fun isPlaying(): Boolean {
        return controller?.isPlaying == true
    }

    fun seekTo(position: Long) {
        controller?.seekTo(position)
    }

    fun getCurrentPosition(): Long {
        return controller?.currentPosition ?: 0L
    }

    fun getDuration(): Long {
        return controller?.duration
            ?.takeIf { it >= 0 }
            ?: 0L
    }

//    fun next() {
//        controller?.seekToNext()
//    }

    fun next() {
        controller?.seekToNextMediaItem()
    }

//    fun previous() {
//        controller?.seekToPrevious()
//    }

    fun previous() {
        controller?.seekToPreviousMediaItem()
    }

    fun release() {
        musicController.release()
    }

    // =========================
    // SHUFFLE
    // =========================

    fun setShuffle(enabled: Boolean) {
        controller?.shuffleModeEnabled = enabled
    }

    fun isShuffleEnabled(): Boolean {
        return controller?.shuffleModeEnabled == true
    }

    fun setRepeatMode(mode: Int) {
        controller?.repeatMode = mode
    }

    fun playAt(index: Int) {

        val player = controller ?: return

        if (
            index < 0 ||
            index >= player.mediaItemCount
        ) {
            return
        }

        player.seekTo(
            index,
            0L
        )

        player.play()
    }
}