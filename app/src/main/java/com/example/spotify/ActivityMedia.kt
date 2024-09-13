package com.example.spotify

import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.IOException
import java.util.concurrent.TimeUnit

class ActivityMedia : AppCompatActivity() {
    private var play: Button? = null
    private var stop: Button? = null
    private var forward: Button? = null
    private var rewind: Button? = null
    private var mediaPlayer: MediaPlayer? = null
    private var seekBar: SeekBar? = null
    private var tvInfo: TextView? = null

    private val handler = Handler(Looper.getMainLooper())
    private val updateSeekBarRunnable = object : Runnable {
        override fun run() {
            mediaPlayer?.let {
                val currentPosition = it.currentPosition
                seekBar?.progress = currentPosition

                // Actualiza el tiempo mostrado en el TextView
                val formattedTime = formatTime(currentPosition)
                tvInfo?.text = "Archivo: MIATA ᕙ(⇀‸↼‶)ᕗ - $formattedTime"

                handler.postDelayed(this, 1000) // Actualiza cada segundo
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media2)

        play = findViewById(R.id.btn_reproducir)
        stop = findViewById(R.id.btn_detener)
        forward = findViewById(R.id.btn_adelantar)
        rewind = findViewById(R.id.btn_retroceder)
        seekBar = findViewById(R.id.seekBar)
        tvInfo = findViewById(R.id.tv_info)

        play?.setOnClickListener {
            mediaPlayer = MediaPlayer().apply {
                setAudioStreamType(AudioManager.STREAM_MUSIC)
                val uri = Uri.parse("android.resource://${packageName}/${R.raw.miata}")
                try {
                    setDataSource(this@ActivityMedia, uri)
                    prepare()
                    start()

                    // Configura la barra de progreso con la duración total del audio
                    seekBar?.max = duration
                    handler.post(updateSeekBarRunnable) // Inicia la actualización del SeekBar

                    Toast.makeText(this@ActivityMedia, "Comienza reproducción", Toast.LENGTH_LONG).show()
                } catch (e: IOException) {
                    Toast.makeText(this@ActivityMedia, "Error al reproducir", Toast.LENGTH_LONG).show()
                }
            }
        }

        stop?.setOnClickListener {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = null
                handler.removeCallbacks(updateSeekBarRunnable) // Detén la actualización del SeekBar
                seekBar?.progress = 0 // Reinicia la barra de progreso
                Toast.makeText(this@ActivityMedia, "Reproducción detenida", Toast.LENGTH_LONG).show()
            }
        }

        // Adelantar 10 segundos
        forward?.setOnClickListener {
            mediaPlayer?.let {
                val newPosition = it.currentPosition + 10000
                it.seekTo(newPosition.coerceAtMost(it.duration)) // Asegura que no se pase del final
                seekBar?.progress = it.currentPosition
            }
        }

        // Retroceder 10 segundos
        rewind?.setOnClickListener {
            mediaPlayer?.let {
                val newPosition = it.currentPosition - 10000
                it.seekTo(newPosition.coerceAtLeast(0)) // Asegura que no se pase del principio
                seekBar?.progress = it.currentPosition
            }
        }

        // Permitir al usuario mover manualmente la barra de progreso
        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer?.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun formatTime(milliseconds: Int): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds.toLong())
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds.toLong()) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}