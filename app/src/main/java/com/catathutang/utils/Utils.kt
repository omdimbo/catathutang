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
            v >= 1_000_000     -> "Rp ${String.format("%.1f", v / 1_000_000.0)}jt"
            else               -> "Rp " + NumberFormat.getNumberInstance(idLocale).format(v)
        }
    }

    fun fmtFull(n: Double): String =
        "Rp " + NumberFormat.getNumberInstance(idLocale).format(n.roundToInt())
}

object AvatarHelper {
    // Vivid neon-friendly palette for dark theme
    private val bgColors   = listOf("#0D2A35", "#0A1F10", "#1A1030", "#1A1200")
    private val textColors = listOf("#00E5FF", "#00E676", "#7C4DFF", "#FFD600")

    fun bgColor(name: String): Int  = Color.parseColor(bgColors[name[0].code % 4])
    fun textColor(name: String): Int = Color.parseColor(textColors[name[0].code % 4])
    fun initials(name: String): String =
        name.trim().split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("").uppercase()
}

object CategoryHelper {
    data class CatInfo(val icon: String, val bgColor: String, val textColor: String)

    private val map = mapOf(
        "hp"      to CatInfo("📱", "#0D2A35", "#00E5FF"),
        "motor"   to CatInfo("🏍", "#1A1200", "#FFD600"),
        "barang"  to CatInfo("📦", "#1A1030", "#7C4DFF"),
        "lainnya" to CatInfo("📋", "#0D1A0A", "#00E676")
    )

    fun get(key: String): CatInfo = map[key] ?: map["lainnya"]!!
}

// 15 rotating quotes — auto-cycle every 5 seconds
val quotes = listOf(
    Pair("Hutang adalah beban hati yang tak terlihat, tapi selalu terasa.", "— Pepatah Bijak"),
    Pair("Jangan biarkan hutang menunggu. Semakin cepat dilunasi, semakin ringan hidupmu.", "— Robert Kiyosaki"),
    Pair("Menepati janji hutang adalah tanda orang berakal dan berakhlak.", "— Ali bin Abi Thalib"),
    Pair("Catat setiap sen yang kamu pinjam. Ingatan manusia mudah lupa, tapi catatan tidak.", "— Warren Buffett"),
    Pair("Lunasi hutangmu sebelum tidur, agar mimpimu pun tenang.", "— Anonim"),
    Pair("Orang yang meminjam uang harus ingat, tapi orang yang meminjamkan sering lupa.", "— Benjamin Franklin"),
    Pair("Hutang bukan solusi, tapi bisa jadi beban seumur hidup jika tak dikelola.", "— Dave Ramsey"),
    Pair("Kebebasan finansial dimulai dari satu langkah: catat dan lunasi hutangmu.", "— Suze Orman"),
    Pair("Jangan habiskan uang yang belum kamu miliki untuk membeli sesuatu yang tidak kamu butuhkan.", "— Will Rogers"),
    Pair("Hutang adalah perbudakan bagi orang merdeka.", "— Publilius Syrus"),
    Pair("Pengeluaran lebih kecil dari pendapatan adalah rahasia kekayaan.", "— Benjamin Franklin"),
    Pair("Bebaskan dirimu dari hutang lebih cepat dari yang kamu rencanakan.", "— T. Harv Eker"),
    Pair("Orang kaya membuat aset, orang miskin membuat hutang.", "— Robert Kiyosaki"),
    Pair("Tidak ada yang lebih mahal dari uang yang dipinjam.", "— Pepatah Inggris"),
    Pair("Setiap rupiah yang kamu lunasi hari ini adalah langkah menuju kebebasanmu esok.", "— Anonim")
)
