package net.`in`.projecto.layer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date

class CheckoutActivity : AppCompatActivity() {
    private val db = Firebase.firestore
    private lateinit var productName: TextView
    private lateinit var productPrice:TextView
    private lateinit var productImg:ImageView
    private lateinit var data:SkinsObj
    private val auth = Firebase.auth
    private var modelName:String? = null
    private var productId:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        productName = findViewById(R.id.productName)
        productImg = findViewById(R.id.productImg)
        productPrice = findViewById(R.id.productPrice)
        val topAppBar = findViewById<Toolbar>(R.id.topAppBar)
        setSupportActionBar(topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val getBundle = intent.extras
        productId = getBundle?.getString("item")
        val sharedPref = this.getSharedPreferences("model_name", Context.MODE_PRIVATE)
        modelName = sharedPref.getString(getString(R.string.mobile_model),"")

        val currentUser = auth.currentUser

        val checkoutBtn = findViewById<Button>(R.id.checkoutBtn)
        checkoutBtn.setOnClickListener {

          if (currentUser != null){

              saveOrder(skinObj = data, auth = auth)


          }else{
              try {
                  val bundle = Bundle()
                  bundle.putString("productId","$productId")
                  val intent = Intent(this,ProfileActivity::class.java)
                  intent.putExtras(bundle)
                  startActivity(intent)

              }catch (e:Exception){
                  e.printStackTrace()
              }
          }


        }

        val fetchOrder = Runnable {
            run {
                db.collection("skins").get()
                    .addOnSuccessListener {snapshots->
                        for (document in snapshots){
                            if(document.id == productId){
                                data = document.toObject(SkinsObj::class.java)
                                updateUi(data)

                            }
                        }
                    }
                    .addOnFailureListener {e->
                        e.printStackTrace()
                    }
            }
            }

        val fetchThread = Thread(fetchOrder)
        fetchThread.start()
        }

    private fun updateUi(skinsObj: SkinsObj){
      skinsObj.apply {
          Picasso.get()
              .load(this.img)
              .into(productImg)

          productPrice.text = this.price
          productName.text = this.name

      }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun saveOrder(skinObj: SkinsObj,auth:FirebaseAuth) {

        val s = SimpleDateFormat("dd/MM/yyyy")
        val format: String = s.format(Date())

        val data = hashMapOf(
            "customer" to "${auth.currentUser?.uid}",
            "product" to skinObj.product_id,
            "date" to format,
            "device_model" to "$modelName",
            "product_img" to skinObj.img,
            "product_name" to skinObj.name,
            "product_price" to skinObj.price
        )

        val saveData = Runnable {
            run {
                db.collection("order").add(data)
                    .addOnSuccessListener {
                        val bundle = Bundle()
                        bundle.putString("productId","$productId")
                        val intent = Intent(this,StatusActivity::class.java)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        Log.d("BOOK","order failed")
                    }
            }
        }

        Thread(saveData).start()
    }

 }