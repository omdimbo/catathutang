package com.catathutang.ui.cicilan

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.catathutang.databinding.FragmentCicilanDetailBinding
import com.catathutang.utils.CategoryHelper
import com.catathutang.utils.Formatter
import kotlin.math.ceil

class CicilanDetailFragment : Fragment() {

    private var _binding: FragmentCicilanDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CicilanViewModel by viewModels()
    private val args: CicilanDetailFragmentArgs by navArgs()
    private lateinit var payAdapter: PaymentHistoryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCicilanDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        payAdapter = PaymentHistoryAdapter()
        binding.recyclerPayments.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerPayments.adapter = payAdapter

        viewModel.allCicilan.observe(viewLifecycleOwner) { list ->
            val cicilan = list.find { it.id == args.cicilanId } ?: return@observe
            renderDetail(cicilan)
        }
    }

    private fun renderDetail(c: com.catathutang.data.model.Cicilan) {
        val ctx = requireContext()
        val cat = CategoryHelper.get(c.kategori)
        val lunas = c.isLunas

        binding.tvTitle.text = c.nama
        binding.tvHeroIcon.text = cat.icon
        binding.tvHeroName.text = c.nama
        binding.tvHeroSub.text = if (c.dari.isNotEmpty()) c.dari else "Cicilan pribadi"

        val heroColor = if (lunas) Color.parseColor("#3B6D11") else Color.parseColor("#E24B4A")
        binding.heroCard.setBackgroundColor(heroColor)

        binding.progressHero.progress = c.pct
        val blnLagi = if (lunas) 0 else ceil(c.sisaAmt / c.perBulan).toInt()
        binding.tvPct.text = "${c.pct}% terbayar"
        binding.tvBlnLagi.text = if (lunas) "Lunas!" else "$blnLagi bln lagi"

        // Stats grid
        binding.tvStatTotal.text = Formatter.fmt(c.total)
        binding.tvStatPaid.text = Formatter.fmt(c.paidSum)
        binding.tvStatSisa.text = Formatter.fmt(c.sisaAmt)
        binding.tvStatPerBulan.text = Formatter.fmt(c.perBulan)
        binding.tvStatTenor.text = "${c.tenor} bln"
        binding.tvStatBayarKe.text = "${c.paid.size}/${c.tenor}"

        // Payment list
        binding.tvHistLabel.text = "Riwayat (${c.paid.size} pembayaran)"
        payAdapter.submitList(c.paid.reversed().mapIndexed { i, amt ->
            val no = c.paid.size - i
            val sisaAfter = maxOf(0.0, c.total - c.paid.take(no).sum())
            PaymentItem(no, amt, sisaAfter)
        })
        binding.emptyPayments.visibility = if (c.paid.isEmpty()) View.VISIBLE else View.GONE

        binding.btnBayar.setOnClickListener {
            val input = binding.etPayment.text.toString().toDoubleOrNull() ?: 0.0
            if (input <= 0) {
                Toast.makeText(ctx, "Masukkan nominal!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (input > c.sisaAmt) {
                Toast.makeText(ctx, "Melebihi sisa hutang!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.addPayment(c, input)
            binding.etPayment.setText("")
            val msg = if (c.paid.sum() + input >= c.total) "Selamat! Cicilan lunas! 🎉" else "Pembayaran dicatat!"
            Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
        }

        binding.tvUndo.setOnClickListener {
            if (c.paid.isEmpty()) {
                Toast.makeText(ctx, "Tidak ada pembayaran", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.undoPayment(c)
                Toast.makeText(ctx, "Dibatalkan", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnHapus.setOnClickListener {
            AlertDialog.Builder(ctx)
                .setTitle("Hapus Cicilan")
                .setMessage("Hapus cicilan \"${c.nama}\"?")
                .setPositiveButton("Hapus") { _, _ ->
                    viewModel.delete(c)
                    Toast.makeText(ctx, "Cicilan dihapus", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                .setNegativeButton("Batal", null)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
