package com.catathutang.ui.hutang

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.catathutang.data.model.Hutang
import com.catathutang.databinding.ItemHutangBinding
import com.catathutang.utils.AvatarHelper
import com.catathutang.utils.Formatter

class HutangAdapter(
    private val onItemClick: (Hutang) -> Unit
) : ListAdapter<Hutang, HutangAdapter.VH>(DIFF) {

    inner class VH(private val b: ItemHutangBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: Hutang) {
            b.tvAvatar.text = AvatarHelper.initials(item.nama)
            b.tvAvatar.setBackgroundColor(AvatarHelper.bgColor(item.nama))
            b.tvAvatar.setTextColor(AvatarHelper.textColor(item.nama))
            b.tvName.text = item.nama
            b.tvDesc.text = buildString {
                if (item.keterangan.isNotEmpty()) append(item.keterangan)
                append(" · ${item.kategori}")
            }
            val color = if (item.type == "h") b.root.context.getColor(com.catathutang.R.color.red)
                        else b.root.context.getColor(com.catathutang.R.color.green)
            b.tvAmount.setTextColor(color)
            val prefix = if (item.type == "h") "-" else "+"
            b.tvAmount.text = "$prefix${Formatter.fmt(item.nominal)}"
            b.tvBadge.text = if (item.type == "h") "Hutang" else "Piutang"
            val (bg, tc) = if (item.type == "h")
                Pair(com.catathutang.R.color.red_light, com.catathutang.R.color.red_dark)
            else
                Pair(com.catathutang.R.color.green_light, com.catathutang.R.color.green_dark)
            b.tvBadge.setBackgroundColor(b.root.context.getColor(bg))
            b.tvBadge.setTextColor(b.root.context.getColor(tc))
            b.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemHutangBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Hutang>() {
            override fun areItemsTheSame(a: Hutang, b: Hutang) = a.id == b.id
            override fun areContentsTheSame(a: Hutang, b: Hutang) = a == b
        }
    }
}
