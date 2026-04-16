package com.catathutang.ui.investasi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.catathutang.databinding.ItemSahamBinding
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs

class SahamAdapter : ListAdapter<SahamUiItem, SahamAdapter.VH>(DIFF) {

    inner class VH(private val b: ItemSahamBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: SahamUiItem) {
            val ctx = b.root.context
            b.tvTicker.text = item.symbol
            b.tvKode.text = item.symbol
            b.tvNama.text = item.namaPerusahaan

            if (item.isError) {
                b.tvHarga.text = "—"
                b.tvChange.text = "Gagal"
                b.tvChange.setTextColor(ctx.getColor(com.catathutang.R.color.text_secondary))
                return
            }

            val fmt = NumberFormat.getNumberInstance(Locale("id", "ID"))
            b.tvHarga.text = when (item.currency) {
                "IDR" -> "Rp ${fmt.format(item.harga.toLong())}"
                else -> "$${String.format("%.2f", item.harga)}"
            }

            val up = item.changePercent >= 0
            b.tvChange.text = "${if (up) "▲" else "▼"} ${String.format("%.2f", abs(item.changePercent))}%"
            b.tvChange.setTextColor(
                if (up) ctx.getColor(com.catathutang.R.color.green)
                else ctx.getColor(com.catathutang.R.color.red)
            )

            // Volume in millions
            val volText = when {
                item.volume >= 1_000_000 -> "${String.format("%.1f", item.volume / 1_000_000.0)}M"
                item.volume >= 1_000 -> "${String.format("%.1f", item.volume / 1_000.0)}K"
                else -> item.volume.toString()
            }
            b.tvVolume.text = "Vol: $volText"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemSahamBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<SahamUiItem>() {
            override fun areItemsTheSame(a: SahamUiItem, b: SahamUiItem) = a.symbol == b.symbol
            override fun areContentsTheSame(a: SahamUiItem, b: SahamUiItem) = a == b
        }
    }
}
