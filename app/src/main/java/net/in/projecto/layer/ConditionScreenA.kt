package net.`in`.projecto.layer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ConditionScreenA : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_condition_screen)
        val nxtBtn = findViewById<Button>(R.id.nxtBtn)
        nxtBtn.setOnClickListener {
            try {
                val intent = Intent(this,ConditionScreenB::class.java)
                startActivity(intent)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
}