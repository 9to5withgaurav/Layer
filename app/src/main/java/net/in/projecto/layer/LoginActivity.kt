package net.`in`.projecto.layer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val mobileInput = findViewById<EditText>(R.id.mobileInput)

        val loginBtn = findViewById<Button>(R.id.loginBtn)

        loginBtn.setOnClickListener {
            try {
                val getMobileInput = mobileInput.text.toString()
                val bundle = Bundle()
                bundle.putString("MOBILE",getMobileInput)
                val intent = Intent(this,OtpVerify::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }catch (e:Exception){
                e.printStackTrace()
            }

        }
    }
    
}