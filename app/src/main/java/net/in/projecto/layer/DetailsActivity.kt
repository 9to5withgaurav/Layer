package net.`in`.projecto.layer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.room.Room
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class DetailsActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    private lateinit var skinImg: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val topAppBar = findViewById<Toolbar>(R.id.topAppBar)
        setSupportActionBar(topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        skinImg = findViewById(R.id.skin_img_placeholder)
        val getBundle = intent.extras
        val getProductId = getBundle?.getString("item")
        val addedToCartBtn = findViewById<Button>(R.id.addedBtn)
        addedToCartBtn.setOnClickListener {
            try {
                val intent = Intent(this,CheckoutActivity::class.java)
                if (getBundle != null) {
                    intent.putExtras(getBundle)
                }
                startActivity(intent)
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }

    val fetchSkinInfo = Runnable {
        run {
            db.collection("skins").get()
                .addOnSuccessListener {snapshots->
                    for (document in snapshots){
                        if(document.id == getProductId){
                            val data = document.toObject(SkinsObj::class.java)
                            updateUi(data)

                        }
                    }
                }
                .addOnFailureListener {e->
                    e.printStackTrace()
                }
        }
    }

        val thread = Thread(fetchSkinInfo)
        thread.start()

    }

    private fun updateUi(skinsObj: SkinsObj){
     Picasso.get()
         .load(skinsObj.img)
         .into(skinImg)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}