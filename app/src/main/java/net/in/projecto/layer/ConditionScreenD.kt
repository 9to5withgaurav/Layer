package net.`in`.projecto.layer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ConditionScreenD : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_condition_screen_d)
        val nxtBtn = findViewById<Button>(R.id.nxt_btn)
        nxtBtn.setOnClickListener {
            try {
                val intent = Intent(this, CheckDeviceModel::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}