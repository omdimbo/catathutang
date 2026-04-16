package com.catathutang.ui.cicilan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.catathutang.databinding.ItemPaymentBinding
import com.catathutang.utils.Formatter

data class PaymentItem(val no: Int, val amount: Double, val sisaAfter: Double)

class PaymentHistoryAdapter : ListAdapter<PaymentItem, PaymentHistoryAdapter.VH>(DIFF) {

    inner class VH(private val b: ItemPaymentBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: PaymentItem) {
            b.tvPayNo.text = "Pembayaran ke-${item.no}"
            b.tvPayLabel.text = "Cicilan bulan ${item.no}"
            b.tvPayAmount.text = Formatter.fmtFull(item.amount)
            b.tvPaySisa.text = "Sisa: ${Formatter.fmt(item.sisaAfter)}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<PaymentItem>() {
            override fun areItemsTheSame(a: PaymentItem, b: PaymentItem) = a.no == b.no
            override fun areContentsTheSame(a: PaymentItem, b: PaymentItem) = a == b
        }
    }
}
