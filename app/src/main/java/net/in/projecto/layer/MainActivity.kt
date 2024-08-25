package net.`in`.projecto.layer

import ItemClickListener
import SkinAdapter
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private val skinsList = mutableListOf<SkinsObj>()
    private val db = Firebase.firestore
    private lateinit var adapter:SkinAdapter
    private var deviceModel: String = android.os.Build.MODEL


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)

        val searchBox = findViewById<TextView>(R.id.searchEditText)
        searchBox.hint = resources.getString(R.string.search_hint,deviceModel)
        val sharedPref = getSharedPreferences(getString(R.string.tutorial_preference_key), Context.MODE_PRIVATE) ?: return
        val getTutorialAction = sharedPref.getString(getString(R.string.tutorial_show),"enable")
        if(getTutorialAction == "enable"){
          try {
              val intent = Intent(this,ConditionScreenA::class.java)
              startActivity(intent)
          }catch (e:Exception){
              e.printStackTrace()
          }
        }



        val skinsRecycle = findViewById<RecyclerView>(R.id.recyclerView)
        skinsRecycle.layoutManager = GridLayoutManager(this,2)
        adapter = SkinAdapter(skinsList, object : ItemClickListener {
            override fun click(skinObj: SkinsObj) {
                try {
                    val bundle = Bundle()
                    bundle.putString("item",skinObj.product_id)
                    val intent = Intent(this@MainActivity,DetailsActivity::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }catch (e:Exception){
                    e.printStackTrace()
                }

            }

        })
        skinsRecycle.adapter = adapter

        fetchRecycleViewThread()

    }

    override fun onStop() {
        super.onStop()
        val sharedPref = getSharedPreferences(getString(R.string.tutorial_preference_key), Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()){
           putString(getString(R.string.tutorial_show),"disable")
           apply()
        }
    }

    private fun fetchRecycleViewThread(){
        val myThread = Thread {
            db.collection("skins")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val data = document.toObject(SkinsObj::class.java)
                        skinsList.add(data)


                    }
                    adapter.notifyDataSetChanged()

                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                }
        }

        myThread.start()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
      return  when(item.itemId){

          R.id.cart -> {
              try {
                  val intent = Intent(this,OrderActivity::class.java)
                  startActivity(intent)
              }catch (e:Exception){
                  e.printStackTrace()
              }
              true
          }

          else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.top_bar, menu)
        return true
    }
}