package com.catathutang.ui.investasi

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.catathutang.data.model.Komoditas
import com.catathutang.databinding.ItemKomoditasBinding
import com.catathutang.utils.Formatter
import kotlin.math.abs

class KomoditasAdapter : ListAdapter<Komoditas, KomoditasAdapter.VH>(DIFF) {

    inner class VH(private val b: ItemKomoditasBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: Komoditas) {
            val ctx = b.root.context
            b.tvIcon.text = item.icon
            b.ivIconBg.setBackgroundColor(Color.parseColor(item.bgColor))
            b.tvName.text = item.nama
            b.tvUnit.text = item.unit
            b.tvPrice.text = if (item.harga >= 1_000_000) Formatter.fmtFull(item.harga) else Formatter.fmt(item.harga)
            val up = item.chg >= 0
            val changeText = "${if (up) "▲" else "▼"} ${String.format("%.2f", abs(item.chg))}%"
            b.tvChange.text = changeText
            b.tvChange.setTextColor(
                if (up) ctx.getColor(com.catathutang.R.color.green)
                else ctx.getColor(com.catathutang.R.color.red)
            )
            val range = item.high - item.low
            val pos = if (range > 0) ((item.harga - item.low) / range * 100).toInt().coerceIn(0, 100) else 50
            b.progressRange.progress = pos
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemKomoditasBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Komoditas>() {
            override fun areItemsTheSame(a: Komoditas, b: Komoditas) = a.nama == b.nama
            override fun areContentsTheSame(a: Komoditas, b: Komoditas) = a == b
        }
    }
}
