package net.`in`.projecto.layer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ConditionScreenB : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_condition_screen_b)
        val nxtBtn = findViewById<Button>(R.id.nxt_btn)
        nxtBtn.setOnClickListener {
            try {
                val intent = Intent(this,ConditionScreenC::class.java)
                startActivity(intent)
            }catch (e:Exception){
                e.printStackTrace()
            }

        }

    }
}