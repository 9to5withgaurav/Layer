package net.`in`.projecto.layer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ProfileActivity : AppCompatActivity() {

    private val db = Firebase.firestore

    init {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

      val name = findViewById<EditText>(R.id.nameInput)
      val address = findViewById<EditText>(R.id.addInput)
      val pinCode = findViewById<EditText>(R.id.pinInput)

        if(Firebase.auth.currentUser == null){
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

      val sharedPref = getSharedPreferences("CID", Context.MODE_PRIVATE)?:return
      val getUserId = sharedPref.getString("customer",null)
      Log.d("UID","$getUserId")
      val saveBtn = findViewById<Button>(R.id.saveBtn)

        saveBtn.setOnClickListener {

                Thread(Runnable {
                    run {
                        val data = hashMapOf<String,String>(
                            "name" to "${name.text}",
                            "address" to "${address.text}",
                            "pincode" to "${pinCode.text}",
                            "customer" to "$getUserId"

                        )
                        db.collection("customer").document("$getUserId")
                            .set(data)
                            .addOnSuccessListener {
                                val intent = Intent(this,MainActivity::class.java)
                                startActivity(intent)
                            }
                            .addOnFailureListener {
                                Log.d("CUSTOMER","failed")
                            }
                    }
                }).start()

        }

    }
}