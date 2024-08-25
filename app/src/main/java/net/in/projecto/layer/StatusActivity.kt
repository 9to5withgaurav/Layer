package net.`in`.projecto.layer

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date

class StatusActivity : AppCompatActivity() {
    private val auth = Firebase.auth
    private val db = Firebase.firestore
    private lateinit var productImg:ImageView
    private lateinit var productName:TextView
    private lateinit var productPrice:TextView
    private lateinit var msg:TextView
    private lateinit var placeDate:TextView
    private var deviceModel:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)
        val topAppBar = findViewById<Toolbar>(R.id.topAppBar)
        setSupportActionBar(topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val bundle = intent.extras
        val getProductId = bundle?.getString("productId")
        productImg = findViewById(R.id.productImg)
        productName = findViewById(R.id.productName)
        productPrice = findViewById(R.id.productPrice)
        msg = findViewById(R.id.confirmationMsg)
        placeDate = findViewById(R.id.placeDate)
        val currentUser = auth.currentUser?.uid

        val deviceSharedPref = getSharedPreferences("model_name",Context.MODE_PRIVATE)
        deviceModel = deviceSharedPref.getString(getString(R.string.mobile_model),"null")

        val fetchProduct = Runnable {
            db.collection("order").get()
                .addOnSuccessListener {snapshots ->
                    for (document in snapshots){
                        if(document["product"] == getProductId && document["customer"] == currentUser){
                            Picasso.get()
                                .load("${document["product_img"]}")
                                .into(productImg)

                            productName.text = document["product_name"].toString()
                            productPrice.text = document["product_price"].toString()
                            placeDate.text = document["date"].toString()
                            msg.text = resources.getString(R.string.confirmation_msg,document["date"])
                        }
                    }
                }
                .addOnFailureListener {e->
                  Log.d("Document","${e.printStackTrace()}")
                }
        }

        Thread(fetchProduct).start()

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }



}
