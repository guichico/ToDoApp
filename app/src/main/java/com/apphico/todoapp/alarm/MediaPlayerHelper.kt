package com.apphico.todoapp.alarm

import android.content.Context
import android.media.MediaPlayer
import android.provider.Settings

interface MediaPlayerHelper {
    fun start()
    fun stop()
}

class MediaPlayerHelperImpl(
    context: Context
) : MediaPlayerHelper {

    val mediaPlayer: MediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_ALARM_ALERT_URI)

    override fun start() {
        mediaPlayer.isLooping = true
        mediaPlayer.start()
    }

    override fun stop() {
        mediaPlayer.stop()
        mediaPlayer.release()
    }
}