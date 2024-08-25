package net.`in`.projecto.layer

import ItemClickListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class OrderAdapter(private val orderList:MutableList<OrderObj>, private val clickItem:ItemClickListener) : RecyclerView.Adapter<OrderAdapter.OrderView>() {
  class OrderView(view: View) : RecyclerView.ViewHolder(view){
      val productImg = view.findViewById<ImageView>(R.id.banner)
      val productName: TextView = view.findViewById(R.id.title)
      val placedStatus = view.findViewById<TextView>(R.id.placedStatus)

  }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderView {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_item,parent,false)
        return OrderView(view)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: OrderView, position: Int) {
        val currentItem = orderList[position]
        holder.productName.text = currentItem.product_name
        holder.placedStatus.text = currentItem.date
        Picasso.get()
            .load(currentItem.product_img)
            .into(holder.productImg)

        holder.itemView.setOnClickListener {
            clickItem.onClick(currentItem)
        }

    }

    interface ItemClickListener{ //using as SAM
        fun onClick(item:OrderObj)
    }
}
