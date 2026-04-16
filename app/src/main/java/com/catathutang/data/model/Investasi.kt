package com.catathutang.data.model

data class Komoditas(
    val nama: String,
    val unit: String,
    val icon: String,
    val bgColor: String,
    val textColor: String,
    var harga: Double,
    var chg: Double,
    val low: Double,
    val high: Double
)

data class Saham(
    val kode: String,
    val nama: String,
    var harga: Int,
    var chg: Double,
    val bgColor: String,
    val textColor: String
)

object InvestasiData {
    val komoditas = mutableListOf(
        Komoditas("Emas", "per gram", "🥇", "#FAEEDA", "#854F0B", 1685000.0, 0.42, 1670000.0, 1690000.0),
        Komoditas("Perak", "per gram", "🥈", "#F1EFE8", "#5F5E5A", 19800.0, -0.18, 19500.0, 20100.0),
        Komoditas("Minyak", "per barel (USD)", "🛢", "#FCEBEB", "#A32D2D", 858000.0, 1.12, 840000.0, 865000.0),
        Komoditas("Bitcoin", "per koin (IDR)", "₿", "#EEEDFE", "#534AB7", 1620000000.0, 2.35, 1580000000.0, 1650000000.0),
        Komoditas("Dolar AS", "per 1 USD", "💵", "#EAF3DE", "#3B6D11", 16350.0, -0.05, 16300.0, 16400.0),
        Komoditas("Reksa Dana", "NAB rata-rata", "📊", "#E6F1FB", "#185FA5", 2450.0, 0.38, 2430.0, 2460.0)
    )

    val saham = mutableListOf(
        Saham("BBCA", "Bank Central Asia", 9875, 1.28, "#E6F1FB", "#185FA5"),
        Saham("TLKM", "Telkom Indonesia", 3180, -0.63, "#FAEEDA", "#854F0B"),
        Saham("ASII", "Astra International", 5200, 0.58, "#EAF3DE", "#3B6D11"),
        Saham("GOTO", "GoTo Gojek Tokopedia", 62, -1.59, "#FCEBEB", "#A32D2D"),
        Saham("BBRI", "Bank Rakyat Indonesia", 4320, 0.93, "#EEEDFE", "#534AB7"),
        Saham("BMRI", "Bank Mandiri", 5875, 0.43, "#E1F5EE", "#0F6E56")
    )
}
