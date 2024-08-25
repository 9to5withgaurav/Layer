package net.`in`.projecto.layer

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class CheckDeviceModel : AppCompatActivity() {

    private var deviceModel: String = android.os.Build.MODEL
    private lateinit var banner: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_device_model)

        banner = findViewById(R.id.banner)
        saveDeviceModelToSharedPrefs(deviceModel)


        Thread(Runnable {
            Thread.sleep(2000)
            run {
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
        }).start()

    }

    private fun saveDeviceModelToSharedPrefs(deviceModel: String) {
        val modelSharedPref = getSharedPreferences("model_name", Context.MODE_PRIVATE)
        with(modelSharedPref.edit()) {
            putString(getString(R.string.mobile_model), deviceModel)
            apply()
        }
    }


}
