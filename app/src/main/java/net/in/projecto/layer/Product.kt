package net.`in`.projecto.layer

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import com.google.firebase.firestore.auth.User

@Entity("product")
data class Product(
    @PrimaryKey(autoGenerate = true) val uid:Int = 0,
    val name: String?,
    val price: String?,
    @ColumnInfo("product_id") val productId: String?,
    val quantity: String?
)
@Dao
interface ProductDAO{
    @Query("SELECT * FROM product")
    fun getAll(): List<Product>

    @Insert
    fun insertAll(vararg product: Product)
}

@Database(entities = [Product::class], version = 1)
abstract class AppDatabase:RoomDatabase(){
    abstract fun productDao():ProductDAO
}