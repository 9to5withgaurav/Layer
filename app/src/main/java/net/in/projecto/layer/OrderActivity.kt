package net.`in`.projecto.layer

import ItemClickListener
import SkinAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class OrderActivity : AppCompatActivity() {
    private val currentUser = Firebase.auth.currentUser
    private val db = Firebase.firestore
    private val orderList = mutableListOf<OrderObj>()
    private lateinit var adapter: OrderAdapter
    override fun onStart() {
        super.onStart()
        if (currentUser == null){
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        val orderRecycle = findViewById<RecyclerView>(R.id.recyclerView)
        orderRecycle.layoutManager = LinearLayoutManager(this)
        adapter = OrderAdapter(orderList, object : OrderAdapter.ItemClickListener {
            override fun onClick(item: OrderObj) {
                try {
                    val bundle = Bundle()
                    bundle.putString("productId", item.product)
                    val intent = Intent(this@OrderActivity,StatusActivity::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }catch (e:Exception){
                    e.printStackTrace()
                }

            }

        })
        orderRecycle.adapter = adapter

        fetchRecycleViewThread()

        }


    private fun fetchRecycleViewThread(){
        val myThread = Thread {
            db.collection("order")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        if (document["customer"] == currentUser?.uid){
                        val data = document.toObject(OrderObj::class.java)
                        orderList.add(data)

                      }
                    }
                    adapter.notifyDataSetChanged()

                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                }
        }

        myThread.start()

    }
}