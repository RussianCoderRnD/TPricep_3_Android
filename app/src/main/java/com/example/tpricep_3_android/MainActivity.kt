package com.example.tpricep_3_android

//05.05.2024 Lugovik O.V.

import SensorData
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException // Импорт, который нужен для работы с IOException
import java.net.HttpURLConnection
import java.net.URL

const val delays = 250// Задержка в милисекундах


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var textViewCount: TextView
    private var count = 0
    private val client = OkHttpClient()
    private val gson = Gson()
    //    private val url = "http://192.168.71.75/"
    private val url = "http://192.168.71.75/test"
    private val url_out = "http://192.168.71.75/upload"
    //    private val ipAddress = "192.168.12.75"
    private val port = "80"

    private lateinit var statusWiFi: ImageView

    private lateinit var imageViewStatus_LG: ImageView
    private lateinit var imageViewStatus_PG: ImageView
    private lateinit var imageViewStatus_LP: ImageView
    private lateinit var imageViewStatus_PP: ImageView
    private lateinit var imageViewStatus_STOP: ImageView
    private lateinit var imageViewStatus_ZX: ImageView
    private lateinit var imageViewStatus_FOG: ImageView

    private lateinit var imageViewChoiceLG: ImageView
    private lateinit var imageViewChoicePG: ImageView
    private lateinit var imageViewChoiceLP: ImageView
    private lateinit var imageViewChoicePP: ImageView
    private lateinit var imageViewChoiceSTOP: ImageView
    private lateinit var imageViewChoiceZX: ImageView
    private lateinit var imageViewChoiceFOG: ImageView

    private lateinit var imageButtonReturn:ImageButton
    private lateinit var statusBatt: ImageView

    private var ret = 0//


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Устанавливаем ориентацию на портретную необходимо импортировать --- import android.content.pm.ActivityInfo
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)

        // Скрыть статусную строку
        View.SYSTEM_UI_FLAG_FULLSCREEN.also { window.decorView.systemUiVisibility = it }
        actionBar?.hide()

        // Проверяем, нужно ли закрыть приложение
        if (intent.getBooleanExtra("EXIT", false)) {
            finish()
        }
        enableEdgeToEdge()

        statusWiFi = findViewById(R.id.statusWiFi)
//        fetchDataFromESP8266()
        // функция закрывает приложение по нажатию определённой картинки
        val closeButton: ImageView = findViewById(R.id.imageExit)
        closeButton.setOnClickListener {
            intent = Intent(applicationContext, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Удаляет все предыдущие активности из стека
            intent.putExtra("EXIT", true)
            startActivity(intent)
        }
        imageViewChoiceLG = findViewById<ImageView>(R.id.imageViewChoiceLG)
        imageViewChoicePG = findViewById<ImageView>(R.id.imageViewChoicePG)
        imageViewChoiceLP = findViewById<ImageView>(R.id.imageViewChoiceLP)
        imageViewChoicePP = findViewById<ImageView>(R.id.imageViewChoicePP)
        imageViewChoiceSTOP = findViewById<ImageView>(R.id.imageViewChoiceSTOP)
        imageViewChoiceZX = findViewById<ImageView>(R.id.imageViewChoiceZX)
        imageViewChoiceFOG = findViewById<ImageView>(R.id.imageViewChoiceFOG)

        imageViewStatus_LG = findViewById<ImageView>(R.id.статус_LG)
        imageViewStatus_PG = findViewById<ImageView>(R.id.статус_PG)
        imageViewStatus_LP = findViewById<ImageView>(R.id.статус_LP)
        imageViewStatus_PP = findViewById<ImageView>(R.id.статус_ок_PP)
        imageViewStatus_STOP = findViewById<ImageView>(R.id.статус_ок_STOP)
        imageViewStatus_ZX = findViewById<ImageView>(R.id.статус_ок_ZX)
        imageViewStatus_FOG = findViewById<ImageView>(R.id.статус_ок_FOG)

//        textViewCount = findViewById(R.id.counter)
        statusWiFi = findViewById<ImageView>(R.id.statusWiFi)
        statusBatt = findViewById<ImageView>(R.id.statusBatt)


        val imageButtonUp = findViewById<ImageButton>(R.id.up)
        val imageButtonDown = findViewById<ImageButton>(R.id.down)
        imageButtonReturn = findViewById<ImageButton>(R.id.кнопкаПереТест)

        val imageViews = mapOf(
            1 to findViewById<ImageView>(R.id.imageViewChoiceLG),
            2 to findViewById<ImageView>(R.id.imageViewChoicePG),
            3 to findViewById<ImageView>(R.id.imageViewChoiceLP),
            4 to findViewById<ImageView>(R.id.imageViewChoicePP),
            5 to findViewById<ImageView>(R.id.imageViewChoiceSTOP),
            6 to findViewById<ImageView>(R.id.imageViewChoiceZX),
            7 to findViewById<ImageView>(R.id.imageViewChoiceFOG)
        )

        imageButtonUp.setOnClickListener {
            updateCount(false, imageViews)
            changeButtonImageTemporarily(
                imageButtonUp,
                R.drawable.arrigthgreenbg,
                R.drawable.arrowgreen
            )
        }

        imageButtonDown.setOnClickListener {
            updateCount(true, imageViews)
            changeButtonImageTemporarily(
                imageButtonDown,
                R.drawable.arrowedredgreenflipbg,
                R.drawable.arrowgreendown
            )
        }
        buttonReset()
        buttonInfo()
        A_NameLines()

    }// КОНЕЦ onCreate

    //функция выводит на экран название линий и символ А
    private fun A_NameLines(){
//        val namesLines = listOf("Левый габарит", "Правый габарит", "Левый поворот", "Правый поворот", "STOP", "Задний ход", "Противотуманка")

//        val SumbolA = listOf("A", "A", "A", "A", "A", "A", "A")
//        val sumbolA = findViewById<TextView>(R.id.sumbolA)
//        val textToShowA = SumbolA.joinToString(separator = "\n")
//        sumbolA.text = textToShowA
    }
    // функция сбрасывающая count в 0, затирающаа все ячейки где может находиться указатель выбранной линии, визуализирует нажатие на клавишу imageButtonReturn
    private fun buttonReset(){
        imageButtonReturn.setOnClickListener {
            fetchDataFromESP8266()
            count = 0
            imageViewChoiceFOG.setImageResource(R.drawable.nan)
            imageViewChoiceZX.setImageResource(R.drawable.nan)
            imageViewChoiceSTOP.setImageResource(R.drawable.nan)
            imageViewChoicePP.setImageResource(R.drawable.nan)
            imageViewChoiceLP.setImageResource(R.drawable.nan)
            imageViewChoicePG.setImageResource(R.drawable.nan)
            imageViewChoiceLG.setImageResource(R.drawable.nan)
            // Меняем изображение при нажатии
            imageButtonReturn.setImageResource(R.drawable.returnshadow)
            // Смена изображения через 1 секунду
            Handler(Looper.getMainLooper()).postDelayed({
                imageButtonReturn.setImageResource(R.drawable.returnes)
            }, delays.toLong())  // Задержка в 1000 миллисекунд (1 секунда)
        }}
    // функция изменяющаа count как в + так и в -
    private fun updateCount(increment: Boolean, imageViews: Map<Int, ImageView>) {
        count = if (increment) (count + 1) % 8 else (count + 7) % 8
        //textViewCount.text = count.toString()
        imageViews.forEach { (key, imageView) ->
            imageView.setImageResource(if (count == key) R.drawable.choice else R.drawable.nan)
        }
        sendDataToESP8266()
        println(count)
    }

    private fun changeButtonImageTemporarily(
        button: ImageButton,
        newImage: Int,
        originalImage: Int
    ) {
        button.setImageResource(newImage)
        Handler(Looper.getMainLooper()).postDelayed({
            button.setImageResource(originalImage)
        }, delays.toLong()) // Время в миллисекундах, через которое изображение кнопки вернётся к исходному
    }
    // функация приёма JSON и обработки
    private fun fetchDataFromESP8266() {
        Thread {
            try {
                val request = Request.Builder().url(url).build()
                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        response.body?.string()?.let {
                            val sensorData = gson.fromJson(it, SensorData::class.java)

                            runOnUiThread {
                                statusImegeViwe(
                                    sensorData.LG,
                                    sensorData.PG,
                                    sensorData.LP,
                                    sensorData.PP,
                                    sensorData.STOP,
                                    sensorData.ZX,
                                    sensorData.FOG,
                                    sensorData.BATT
                                )
                                statusWiFi.setImageResource(R.drawable.wifi)
                            }
                        }
                    }
                }
            }catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    this.statusWiFi.setImageResource(R.drawable.wifi_off)
                    Toast.makeText(this@MainActivity, "Ошибка связи с устройством: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
                Log.e("MainActivity", "Ошибка при отправке или получении данных: ${e.message}", e)
            }
        }.start()
    }

    // функция принимает значения тока по линиям, ранее полученный из JSON. Обрабатываеи и выводит как сами значения так и статус чему они соответствуют в виде иконок
    private fun statusImegeViwe(lg: Float, pg: Float, lp: Float, pp: Float, stop: Float, zx: Float, fog: Float, batt: Int) {

        val KZ = 8.0f// Порог короткого замыкания
        val OBR = 0.15f// Порог разрыва соединения
        val LG_PG_OK = 1.0f// Нормальное значение для LG_PG
        val LG_PG_Koff = 0.40f// Допуск
        val LP_PP_OK = 1.0f// Нормальное значение для LP_PP 2.0f
        val STOP_OK = 1.0f// Нормальное значение для STOP 4.0f
        val ZX_OK = 1.0f// Нормальное значение для ZX 2.0f
        val FOG_OK = 1.0f// Нормальное значение для FOG 2.0f

        if (lg <= OBR) imageViewStatus_LG.setImageResource(R.drawable.obriv)
        if (lg >= KZ) imageViewStatus_LG.setImageResource(R.drawable.fulse)
        if (lg > LG_PG_OK - LG_PG_Koff && lg < LG_PG_OK + LG_PG_Koff) imageViewStatus_LG.setImageResource(
            R.drawable.ok
        )
        if (lg >= OBR && lg <= LG_PG_OK - LG_PG_Koff) imageViewStatus_LG.setImageResource(R.drawable.obriv)
        if (lg >= LG_PG_OK + LG_PG_Koff && lg < KZ) imageViewStatus_LG.setImageResource(R.drawable.vnimaniehi)
        val значенияТока_LG = findViewById<TextView>(R.id.значенияТока_LG)
        значенияТока_LG.text = "${lg}"

        if (pg <= OBR) imageViewStatus_PG.setImageResource(R.drawable.obriv)
        if (pg >= KZ) imageViewStatus_PG.setImageResource(R.drawable.fulse)
        if (pg > LG_PG_OK - LG_PG_Koff && pg < LG_PG_OK + LG_PG_Koff) imageViewStatus_PG.setImageResource(
            R.drawable.ok
        )
        if (pg >= OBR && pg <= LG_PG_OK - LG_PG_Koff) imageViewStatus_PG.setImageResource(R.drawable.vnimanielo)
        if (pg >= LG_PG_OK + LG_PG_Koff && pg < KZ) imageViewStatus_PG.setImageResource(R.drawable.vnimaniehi)
        val значенияТока_PG = findViewById<TextView>(R.id.значенияТока_PG)
        значенияТока_PG.text = "${pg}"

        if (lp <= OBR) imageViewStatus_LP.setImageResource(R.drawable.obriv)
        if (lp >= KZ) imageViewStatus_LP.setImageResource(R.drawable.fulse)
        if (lp > LP_PP_OK - LG_PG_Koff && lp < LP_PP_OK + LG_PG_Koff) imageViewStatus_LP.setImageResource(
            R.drawable.ok
        )
        if (lp >= OBR && lp <= LP_PP_OK - LG_PG_Koff) imageViewStatus_LP.setImageResource(R.drawable.vnimanielo)
        if (lp >= LP_PP_OK + LG_PG_Koff && lp < KZ) imageViewStatus_LP.setImageResource(R.drawable.vnimaniehi)
        val значенияТока_LP = findViewById<TextView>(R.id.значенияТока_LP)
        значенияТока_LP.text = "${lp}"

        if (pp <= OBR) imageViewStatus_PP.setImageResource(R.drawable.obriv)
        if (pp >= KZ) imageViewStatus_PP.setImageResource(R.drawable.fulse)
        if (pp > LP_PP_OK - LG_PG_Koff && pp < LP_PP_OK + LG_PG_Koff) imageViewStatus_PP.setImageResource(
            R.drawable.ok
        )
        if (pp >= OBR && pp <= LP_PP_OK - LG_PG_Koff) imageViewStatus_PP.setImageResource(R.drawable.vnimanielo)
        if (pp >= LP_PP_OK + LG_PG_Koff && pp < KZ) imageViewStatus_PP.setImageResource(R.drawable.vnimaniehi)
        val значенияТока_PP = findViewById<TextView>(R.id.значенияТока_PP)
        значенияТока_PP.text = "${pp}"

        if (stop <= OBR) imageViewStatus_STOP.setImageResource(R.drawable.obriv)
        if (stop >= KZ) imageViewStatus_STOP.setImageResource(R.drawable.fulse)
        if (stop > STOP_OK - LG_PG_Koff && stop < STOP_OK + LG_PG_Koff) imageViewStatus_STOP.setImageResource(
            R.drawable.ok
        )
        if (stop >= OBR && stop <= STOP_OK - LG_PG_Koff) imageViewStatus_STOP.setImageResource(R.drawable.vnimanielo)
        if (stop >= STOP_OK + LG_PG_Koff && stop < KZ) imageViewStatus_STOP.setImageResource(R.drawable.vnimaniehi)
        val значенияТока_STOP = findViewById<TextView>(R.id.значенияТока_STOP)
        значенияТока_STOP.text = "${stop}"

        if (zx <= OBR) imageViewStatus_ZX.setImageResource(R.drawable.obriv)
        if (zx >= KZ) imageViewStatus_ZX.setImageResource(R.drawable.fulse)
        if (zx > ZX_OK - LG_PG_Koff && zx < ZX_OK + LG_PG_Koff) imageViewStatus_ZX.setImageResource(
            R.drawable.ok
        )
        if (zx >= OBR && zx <= ZX_OK - LG_PG_Koff) imageViewStatus_ZX.setImageResource(R.drawable.vnimanielo)
        if (zx >= ZX_OK + LG_PG_Koff && zx < KZ) imageViewStatus_ZX.setImageResource(R.drawable.vnimaniehi)
        val значенияТока_ZX = findViewById<TextView>(R.id.значенияТока_ZX)
        значенияТока_ZX.text = "${zx}"

        if (fog <= OBR) imageViewStatus_FOG.setImageResource(R.drawable.obriv)
        if (fog >= KZ) imageViewStatus_FOG.setImageResource(R.drawable.fulse)
        if (fog > FOG_OK - LG_PG_Koff && fog < FOG_OK + LG_PG_Koff) imageViewStatus_FOG.setImageResource(
            R.drawable.ok
        )
        if (fog >= OBR && fog <= FOG_OK - LG_PG_Koff) imageViewStatus_FOG.setImageResource(R.drawable.vnimanielo)
        if (fog >= FOG_OK + LG_PG_Koff && fog < KZ) imageViewStatus_FOG.setImageResource(R.drawable.vnimaniehi)
        val значенияТока_FOG = findViewById<TextView>(R.id.значенияТока_FOG)
        значенияТока_FOG.text = "${fog}"

        val значенияBatt = findViewById<TextView>(R.id.textStatusBat)
        значенияBatt.text = batt.toString() + "%"
        if (batt >= 0 && batt <= 25) statusBatt.setImageResource(R.drawable.batt_25)
        if (batt > 25 && batt <= 50) statusBatt.setImageResource(R.drawable.batt_50)
        if (batt > 50 && batt <= 75) statusBatt.setImageResource(R.drawable.batt_75)
        if (batt > 75 && batt <= 100) statusBatt.setImageResource(R.drawable.batt_100)
    }

    private fun buttonInfo() {
        val button: ImageView = findViewById(R.id.imageInfo)
        button.setOnClickListener {
            val intent = Intent(this, Help::class.java)
            startActivity(intent)
        }
    }

    // Функция отправки данных на ESP8266
    fun sendDataToESP8266() {
        println("sendDataToESP8266")
        val client = OkHttpClient()

        val jsonData = """
    {
        "count": $count
    }
    """.trimIndent()

        val requestBody = jsonData.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(url_out)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                // Обработка ошибки
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                // Обработка успешного ответа
            }
        })
    }
}