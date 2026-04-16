package com.catathutang.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hutang")
data class Hutang(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nama: String,
    val nominal: Double,
    val type: String,          // "h" = berhutang, "k" = berpiutang
    val keterangan: String = "",
    val kategori: String = "pribadi",
    val lunas: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
