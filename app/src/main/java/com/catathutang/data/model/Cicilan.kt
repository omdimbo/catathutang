package com.catathutang.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "cicilan")
@TypeConverters(PaidListConverter::class)
data class Cicilan(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nama: String,
    val kategori: String = "hp",
    val total: Double,
    val perBulan: Double,
    val tenor: Int,
    val dari: String = "",
    val paid: List<Double> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
) {
    val paidSum: Double get() = paid.sum()
    val sisaAmt: Double get() = maxOf(0.0, total - paidSum)
    val pct: Int get() = if (total > 0) minOf(100, (paidSum / total * 100).toInt()) else 0
    val isLunas: Boolean get() = sisaAmt == 0.0
}

class PaidListConverter {
    @TypeConverter
    fun fromList(list: List<Double>): String = Gson().toJson(list)

    @TypeConverter
    fun toList(json: String): List<Double> {
        val type = object : TypeToken<List<Double>>() {}.type
        return Gson().fromJson(json, type) ?: emptyList()
    }
}
