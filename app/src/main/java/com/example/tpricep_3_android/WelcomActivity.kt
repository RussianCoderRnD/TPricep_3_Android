package com.example.tpricep_3_android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.graphics.Color
import android.content.pm.ActivityInfo
import android.view.View
import android.widget.ImageButton

class WelcomActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Устанавливаем ориентацию на портретную необходимо импортировать --- import android.content.pm.ActivityInfo
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        enableEdgeToEdge()
        setContentView(R.layout.activity_welcom)

        // Скрыть статусную строку
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
//        actionBar?.hide()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val startButton: ImageButton = findViewById(R.id.startButton)
        startButton.setOnClickListener {
            // При нажатии на кнопку запускаем MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Закрываем SplashActivity
        }
        // Установить белый цвет фона
       startButton.setBackgroundColor(Color.WHITE)
    }

}