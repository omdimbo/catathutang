package com.catathutang.ui.investasi

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.catathutang.databinding.ItemKomoditasBinding
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs

class KomoditasAdapter : ListAdapter<KomoditasUiItem, KomoditasAdapter.VH>(DIFF) {

    inner class VH(private val b: ItemKomoditasBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: KomoditasUiItem) {
            val ctx = b.root.context
            b.tvIcon.text = item.icon
            b.ivIconBg.setBackgroundColor(Color.parseColor(item.bgColor))
            b.tvName.text = item.nama
            b.tvUnit.text = item.unit

            if (item.isError) {
                b.tvPrice.text = "—"
                b.tvChange.text = "Gagal memuat"
                b.tvChange.setTextColor(ctx.getColor(com.catathutang.R.color.text_secondary))
                b.progressRange.progress = 50
                return
            }

            // Format price
            val fmt = NumberFormat.getNumberInstance(Locale("id", "ID"))
            b.tvPrice.text = when {
                item.currency == "IDR" -> "Rp ${fmt.format(item.harga.toLong())}"
                item.harga >= 1_000_000 -> "$${fmt.format(item.harga.toLong())}"
                else -> "$${String.format("%.2f", item.harga)}"
            }

            val up = item.changePercent >= 0
            b.tvChange.text = "${if (up) "▲" else "▼"} ${String.format("%.2f", abs(item.changePercent))}%"
            b.tvChange.setTextColor(
                if (up) ctx.getColor(com.catathutang.R.color.green)
                else ctx.getColor(com.catathutang.R.color.red)
            )
            b.progressRange.progressTintList = android.content.res.ColorStateList.valueOf(
                if (up) ctx.getColor(com.catathutang.R.color.green)
                else ctx.getColor(com.catathutang.R.color.red)
            )

            val range = item.dayHigh - item.dayLow
            val pos = if (range > 0) ((item.harga - item.dayLow) / range * 100).toInt().coerceIn(0, 100) else 50
            b.progressRange.progress = pos
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemKomoditasBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<KomoditasUiItem>() {
            override fun areItemsTheSame(a: KomoditasUiItem, b: KomoditasUiItem) = a.symbol == b.symbol
            override fun areContentsTheSame(a: KomoditasUiItem, b: KomoditasUiItem) = a == b
        }
    }
}
