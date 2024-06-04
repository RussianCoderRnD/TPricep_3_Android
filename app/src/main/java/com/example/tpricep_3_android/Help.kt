package com.example.tpricep_3_android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
//import com.example.tpricepandroidapp.MainActivity
import android.content.pm.ActivityInfo
import android.view.View

class Help : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Устанавливаем ориентацию на портретную необходимо импортировать --- import android.content.pm.ActivityInfo
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        if (intent.getBooleanExtra("EXIT", false)) {
            finish()
        }
        enableEdgeToEdge()
        setContentView(R.layout.activity_help)

//        // Скрыть статусную строку
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
//        actionBar?.hide()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val closeButton: ImageView = findViewById(R.id.imageReturn)
        closeButton.setOnClickListener {
            intent = Intent(applicationContext, Help::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Удаляет все предыдущие активности из стека
            intent.putExtra("EXIT", true)
            startActivity(intent)
        }
        //        Handler(Looper.getMainLooper()).postDelayed({
//            // Запускаем MainActivity и закрываем SplashActivity
//            startActivity(Intent(this, MainActivity::class.java))
//            finish()
//        }, 3000) // 3000 мс задержка

//        val startButton: Button = findViewById(R.id.startButton)
//        startButton.setOnClickListener {
//            // При нажатии на кнопку запускаем MainActivity
//            startActivity(Intent(this, Help::class.java))
//            finish() // Закрываем SplashActivity
//        }

//        val button: ImageView = findViewById(R.id.imageReturn)
//        button.setOnClickListener {
//            // Создание Intent для перехода к SecondActivity
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//        }
    }
}