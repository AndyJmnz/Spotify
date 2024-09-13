package com.example.spotify

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Componentes tipo boton
        val botonMedia = findViewById<Button>(R.id.btn_media)
        val botonVideo = findViewById<Button>(R.id.btn_video)

        botonMedia.setOnClickListener { v: View? ->
            val intent = Intent(applicationContext, ActivityMedia::class.java)
            startActivity(intent)
        }
        botonVideo.setOnClickListener { v: View? ->
            val intent = Intent(applicationContext, activity_video::class.java)
            startActivity(intent)
        }
    }
}