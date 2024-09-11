package com.example.spotify

import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.IOException

class ActivityMedia : AppCompatActivity() {
    private var play: Button? = null
    private var stop: Button? = null
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media2)

        play = findViewById(R.id.btn_reproducir)
        stop = findViewById(R.id.btn_detener)

        play?.setOnClickListener {
            mediaPlayer = MediaPlayer().apply {
                setAudioStreamType(AudioManager.STREAM_MUSIC)
                val uri = Uri.parse("android.resource://${packageName}/${R.raw.videotainy}")
                try {
                    setDataSource(this@ActivityMedia, uri)
                    prepare()
                    start()
                    Toast.makeText(
                        this@ActivityMedia, "Comienza reproducción",
                        Toast.LENGTH_LONG
                    ).show()
                } catch (e: IOException) {
                    Toast.makeText(
                        this@ActivityMedia, "Error al reproducir",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        stop?.setOnClickListener {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = null
                Toast.makeText(
                    this@ActivityMedia, "Reproducción detenida",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}