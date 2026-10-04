package fr.gemsofrod.gestion.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

/**
 * Persistance locale : un seul fichier JSON dans le stockage interne de
 * l'app. Aucune donnée ne quitte le téléphone, sauf export volontaire.
 */
class Store(context: Context) {

    private val file = File(context.filesDir, "gestion.json")

    fun load(): AppData =
        if (file.exists()) runCatching { fromJson(file.readText()) }.getOrDefault(AppData())
        else AppData()

    fun save(data: AppData) {
        val tmp = File(file.parentFile, "gestion.json.tmp")
        tmp.writeText(toJson(data))
        tmp.renameTo(file)
    }

    companion object {
        private const val VERSION = 1

        fun toJson(data: AppData): String = JSONObject().apply {
            put("version", VERSION)
            put("nextOrderNumber", data.nextOrderNumber)
            put("products", JSONArray(data.products.map { it.toJson() }))
            put("clients", JSONArray(data.clients.map { it.toJson() }))
            put("orders", JSONArray(data.orders.map { it.toJson() }))
            put("sumupPayments", JSONArray(data.sumupPayments.map { it.toJson() }))
        }.toString(2)

        /** Lève une exception si le texte n'est pas une sauvegarde valide. */
        fun fromJson(text: String): AppData {
            val o = JSONObject(text)
            val products = o.getJSONArray("products").objects().map { it.toProduct() }
            val clients = o.getJSONArray("clients").objects().map { it.toClient() }
            val orders = o.getJSONArray("orders").objects().map { it.toOrder() }
            val next = o.optInt("nextOrderNumber", (orders.maxOfOrNull { it.number } ?: 0) + 1)
            val payments = o.optJSONArray("sumupPayments")?.objects()?.map { it.toPayment() } ?: emptyList()
            return AppData(products, clients, orders, next, payments)
        }

        private fun JSONArray.objects(): List<JSONObject> = (0 until length()).map { getJSONObject(it) }

        private inline fun <reified E : Enum<E>> JSONObject.enum(key: String, default: E): E =
            runCatching { enumValueOf<E>(getString(key)) }.getOrDefault(default)

        private fun Product.toJson() = JSONObject().apply {
            put("id", id); put("name", name); put("category", category.name); put("unit", unit.name)
            put("quantity", quantity); put("cost", cost); put("price", price)
            put("threshold", threshold); put("note", note)
        }

        private fun JSONObject.toProduct() = Product(
            id = getString("id"),
            name = getString("name"),
            category = enum("category", Category.FINE),
            unit = enum("unit", StockUnit.PIECE),
            quantity = optDouble("quantity", 0.0),
            cost = optDouble("cost", 0.0),
            price = optDouble("price", 0.0),
            threshold = optDouble("threshold", 0.0),
            note = optString("note", ""),
        )

        private fun Client.toJson() = JSONObject().apply {
            put("id", id); put("name", name); put("email", email); put("phone", phone)
            put("segment", segment.name); put("note", note)
        }

        private fun JSONObject.toClient() = Client(
            id = getString("id"),
            name = getString("name"),
            email = optString("email", ""),
            phone = optString("phone", ""),
            segment = enum("segment", Segment.PROSPECT),
            note = optString("note", ""),
        )

        private fun OrderLine.toJson() = JSONObject().apply {
            put("productId", productId ?: JSONObject.NULL); put("label", label)
            put("quantity", quantity); put("unitPrice", unitPrice); put("unitCost", unitCost)
        }

        private fun JSONObject.toLine() = OrderLine(
            productId = if (isNull("productId")) null else optString("productId"),
            label = optString("label", ""),
            quantity = optDouble("quantity", 0.0),
            unitPrice = optDouble("unitPrice", 0.0),
            unitCost = optDouble("unitCost", 0.0),
        )

        private fun Order.toJson() = JSONObject().apply {
            put("id", id); put("number", number); put("clientId", clientId ?: JSONObject.NULL)
            put("clientName", clientName); put("date", date); put("status", status.name)
            put("lines", JSONArray(lines.map { it.toJson() }))
            put("deposit", deposit); put("note", note)
            put("dueDate", dueDate ?: JSONObject.NULL); put("paidDate", paidDate ?: JSONObject.NULL)
            put("sumupCheckoutId", sumupCheckoutId ?: JSONObject.NULL)
        }

        private fun JSONObject.toOrder() = Order(
            id = getString("id"),
            number = optInt("number", 0),
            clientId = if (isNull("clientId")) null else optString("clientId"),
            clientName = optString("clientName", ""),
            date = optLong("date"),
            status = enum("status", OrderStatus.DEVIS),
            lines = optJSONArray("lines")?.objects()?.map { it.toLine() } ?: emptyList(),
            deposit = optDouble("deposit", 0.0),
            note = optString("note", ""),
            dueDate = if (isNull("dueDate")) null else optLong("dueDate"),
            paidDate = if (isNull("paidDate")) null else optLong("paidDate"),
            sumupCheckoutId = if (isNull("sumupCheckoutId")) null else optString("sumupCheckoutId"),
        )

        private fun SumUpPayment.toJson() = JSONObject().apply {
            put("code", code); put("amount", amount); put("date", date); put("time", time)
            put("paymentType", paymentType); put("orderId", orderId ?: JSONObject.NULL)
        }

        private fun JSONObject.toPayment() = SumUpPayment(
            code = getString("code"),
            amount = optDouble("amount", 0.0),
            date = optLong("date"),
            time = optString("time", ""),
            paymentType = optString("paymentType", ""),
            orderId = if (isNull("orderId")) null else optString("orderId"),
        )
    }
}
