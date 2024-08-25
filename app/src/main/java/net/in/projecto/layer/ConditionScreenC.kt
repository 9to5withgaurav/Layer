package net.`in`.projecto.layer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ConditionScreenC : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_condition_screen_c)
        val nxtBtn = findViewById<Button>(R.id.nxt_btn)
        nxtBtn.setOnClickListener {
            try {
                val intent = Intent(this,ConditionScreenD::class.java)
                startActivity(intent)
            }catch (e:Exception){
                e.printStackTrace()
            }

        }
    }
}