package com.catathutang.ui.investasi

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.catathutang.data.model.Saham
import com.catathutang.databinding.ItemSahamBinding
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs

class SahamAdapter : ListAdapter<Saham, SahamAdapter.VH>(DIFF) {

    inner class VH(private val b: ItemSahamBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: Saham) {
            val ctx = b.root.context
            b.tvTicker.text = item.kode
            b.tvTicker.setBackgroundColor(Color.parseColor(item.bgColor))
            b.tvTicker.setTextColor(Color.parseColor(item.textColor))
            b.tvKode.text = item.kode
            b.tvNama.text = item.nama
            val fmt = NumberFormat.getNumberInstance(Locale("id", "ID"))
            b.tvHarga.text = "Rp ${fmt.format(item.harga)}"
            val up = item.chg >= 0
            b.tvChange.text = "${if (up) "▲" else "▼"} ${String.format("%.2f", abs(item.chg))}%"
            b.tvChange.setTextColor(
                if (up) ctx.getColor(com.catathutang.R.color.green)
                else ctx.getColor(com.catathutang.R.color.red)
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemSahamBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Saham>() {
            override fun areItemsTheSame(a: Saham, b: Saham) = a.kode == b.kode
            override fun areContentsTheSame(a: Saham, b: Saham) = a == b
        }
    }
}
