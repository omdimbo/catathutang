package com.catathutang.utils

import android.graphics.Color
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs
import kotlin.math.roundToInt

object Formatter {
    private val idLocale = Locale("id", "ID")

    fun fmt(n: Double): String {
        val v = abs(n).roundToInt()
        return when {
            v >= 1_000_000_000 -> "Rp ${String.format("%.1f", v / 1_000_000_000.0)}M"
            v >= 1_000_000 -> "Rp ${String.format("%.1f", v / 1_000_000.0)}jt"
            else -> "Rp " + NumberFormat.getNumberInstance(idLocale).format(v)
        }
    }

    fun fmtFull(n: Double): String =
        "Rp " + NumberFormat.getNumberInstance(idLocale).format(n.roundToInt())
}

object AvatarHelper {
    private val bgColors = listOf("#FCEBEB", "#EAF3DE", "#FAEEDA", "#E6F1FB")
    private val textColors = listOf("#A32D2D", "#3B6D11", "#854F0B", "#185FA5")

    fun bgColor(name: String): Int = Color.parseColor(bgColors[name[0].code % 4])
    fun textColor(name: String): Int = Color.parseColor(textColors[name[0].code % 4])
    fun initials(name: String): String =
        name.trim().split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("").uppercase()
}

object CategoryHelper {
    data class CatInfo(val icon: String, val bgColor: String, val textColor: String)

    private val map = mapOf(
        "hp" to CatInfo("📱", "#E6F1FB", "#185FA5"),
        "motor" to CatInfo("🏍", "#FAEEDA", "#854F0B"),
        "barang" to CatInfo("📦", "#EEEDFE", "#534AB7"),
        "lainnya" to CatInfo("📋", "#F1EFE8", "#5F5E5A")
    )

    fun get(key: String): CatInfo = map[key] ?: map["lainnya"]!!
}

val quotes = listOf(
    Pair("Hutang adalah beban hati yang tak terlihat, tapi selalu terasa.", "— Pepatah bijak"),
    Pair("Jangan biarkan hutang menunggu. Semakin cepat dilunasi, semakin ringan hidupmu.", "— Robert Kiyosaki"),
    Pair("Menepati janji hutang adalah tanda orang berakal dan berakhlak.", "— Ali bin Abi Thalib"),
    Pair("Catat setiap sen yang kamu pinjam. Ingatan manusia mudah lupa, tapi catatan tidak.", "— Warren Buffett"),
    Pair("Lunasi hutangmu sebelum tidur, agar mimpimu pun tenang.", "— Anonim")
)
