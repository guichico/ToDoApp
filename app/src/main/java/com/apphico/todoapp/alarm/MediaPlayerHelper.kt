package com.apphico.todoapp.alarm

import android.content.Context
import android.media.MediaPlayer
import android.provider.Settings

interface MediaPlayerHelper {
    fun start()
    fun stop()
}

class MediaPlayerHelperImpl(
    private val context: Context
) : MediaPlayerHelper {

    var mediaPlayer: MediaPlayer? = null

    override fun start() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_ALARM_ALERT_URI)
        }

        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
    }

    override fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
