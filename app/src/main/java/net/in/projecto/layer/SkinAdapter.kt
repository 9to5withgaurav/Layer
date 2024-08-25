import net.`in`.projecto.layer.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import net.`in`.projecto.layer.SkinsObj

class SkinAdapter(private val skinList:MutableList<SkinsObj>,val clickable:ItemClickListener): RecyclerView.Adapter<SkinAdapter.MySkinViewClass>() {
    class MySkinViewClass(view: View) :RecyclerView.ViewHolder(view) {
        val img:ImageView

        init {
            img = view.findViewById(R.id.skin_img)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySkinViewClass {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.skins_item_layout,parent,false)
        return MySkinViewClass(view)
    }

    override fun getItemCount(): Int {
        return skinList.size
    }

    override fun onBindViewHolder(holder: MySkinViewClass, position: Int) {
        val imgView = holder.img
        Picasso.get()
            .load(skinList[position].img)
            .placeholder(R.drawable.skin_placeholder)
            .into(imgView)

        holder.itemView.setOnClickListener {
            clickable.click(skinList[position])
        }

    }
}

interface ItemClickListener{
   fun click(skinObj : SkinsObj)
}