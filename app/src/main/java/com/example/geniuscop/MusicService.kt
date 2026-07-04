package com.example.geniuscop

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder

class MusicService : Service() {

    private lateinit var mediaPlayer: MediaPlayer
    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.ethereal)
        mediaPlayer.isLooping = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
        return START_STICKY
    }
    override fun onDestroy() {
        mediaPlayer.stop()
        mediaPlayer.release()
        super.onDestroy()
    }
    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    fun pauseMusic() {
        if (mediaPlayer.isPlaying) mediaPlayer.pause()
    }

    fun resumeMusic() {
        if (!mediaPlayer.isPlaying) mediaPlayer.start()
    }
}
