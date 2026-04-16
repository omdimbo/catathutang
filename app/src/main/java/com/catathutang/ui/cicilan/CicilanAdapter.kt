package com.catathutang.ui.cicilan

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.catathutang.data.model.Cicilan
import com.catathutang.databinding.ItemCicilanBinding
import com.catathutang.utils.CategoryHelper
import com.catathutang.utils.Formatter
import kotlin.math.ceil

class CicilanAdapter(
    private val onItemClick: (Cicilan) -> Unit
) : ListAdapter<Cicilan, CicilanAdapter.VH>(DIFF) {

    inner class VH(private val b: ItemCicilanBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: Cicilan) {
            val ctx = b.root.context
            val cat = CategoryHelper.get(item.kategori)

            b.tvCatIcon.text = cat.icon
            b.ivCatBg.setBackgroundColor(Color.parseColor(cat.bgColor))
            b.tvName.text = item.nama
            b.tvMeta.text = buildString {
                if (item.dari.isNotEmpty()) append("${item.dari} · ")
                append("${Formatter.fmt(item.perBulan)}/bln")
            }

            val sisaColor = if (item.isLunas)
                ctx.getColor(com.catathutang.R.color.green)
            else
                ctx.getColor(com.catathutang.R.color.red)
            b.tvSisa.setTextColor(sisaColor)
            b.tvSisa.text = Formatter.fmt(item.sisaAmt)

            // Progress bar
            b.progressBar.progress = item.pct

            val blnLagi = if (item.isLunas) 0 else ceil(item.sisaAmt / item.perBulan).toInt()
            val statusText = "${item.pct}% lunas · ${if (item.isLunas) "Selesai" else "$blnLagi bln lagi"}"
            b.tvProgress.text = statusText

            val badgeText = if (item.isLunas) "Lunas" else "Aktif"
            b.tvBadge.text = badgeText
            if (item.isLunas) {
                b.tvBadge.setBackgroundColor(ctx.getColor(com.catathutang.R.color.green_light))
                b.tvBadge.setTextColor(ctx.getColor(com.catathutang.R.color.green_dark))
            } else {
                b.tvBadge.setBackgroundColor(ctx.getColor(com.catathutang.R.color.red_light))
                b.tvBadge.setTextColor(ctx.getColor(com.catathutang.R.color.red_dark))
            }

            b.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemCicilanBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Cicilan>() {
            override fun areItemsTheSame(a: Cicilan, b: Cicilan) = a.id == b.id
            override fun areContentsTheSame(a: Cicilan, b: Cicilan) = a == b
        }
    }
}
