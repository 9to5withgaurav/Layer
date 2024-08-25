package net.`in`.projecto.layer

data class SkinsObj(
    val img: String = "",
    val name: String = "",
    val price: String = "",
    val product_id: String = "",
    val quantity: String = ""
)

data class OrderObj(
    val customer: String = "",
    val date: String = "",
    val device_model: String = "",
    val product:String = "",
    val product_img: String = "",
    val product_name: String = "",
    val product_price: String = ""
)