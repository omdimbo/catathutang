package com.catathutang.ui.investasi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.catathutang.R
import com.catathutang.data.model.InvestasiData
import com.catathutang.databinding.FragmentInvestasiBinding
import kotlin.random.Random

class InvestasiFragment : Fragment() {

    private var _binding: FragmentInvestasiBinding? = null
    private val binding get() = _binding!!
    private lateinit var komoditasAdapter: KomoditasAdapter
    private lateinit var sahamAdapter: SahamAdapter
    private var currentTab = "komoditas"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentInvestasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        komoditasAdapter = KomoditasAdapter()
        sahamAdapter = SahamAdapter()

        binding.tabKomoditas.setOnClickListener { switchTab("komoditas") }
        binding.tabSaham.setOnClickListener { switchTab("saham") }
        binding.btnRefresh.setOnClickListener { refreshSaham() }

        switchTab("komoditas")
    }

    private fun switchTab(tab: String) {
        currentTab = tab
        val ctx = requireContext()

        if (tab == "komoditas") {
            binding.tabKomoditas.setBackgroundResource(R.drawable.tab_active_bg)
            binding.tabKomoditas.setTextColor(ctx.getColor(R.color.white))
            binding.tabSaham.setBackgroundResource(R.drawable.tab_inactive_bg)
            binding.tabSaham.setTextColor(ctx.getColor(R.color.text_secondary))

            binding.recyclerInvestasi.layoutManager = GridLayoutManager(requireContext(), 2)
            binding.recyclerInvestasi.adapter = komoditasAdapter
            komoditasAdapter.submitList(InvestasiData.komoditas.toList())
            binding.btnRefresh.visibility = View.GONE
            binding.sahamNote.text = getString(R.string.note_komoditas)
        } else {
            binding.tabSaham.setBackgroundResource(R.drawable.tab_active_bg)
            binding.tabSaham.setTextColor(ctx.getColor(R.color.white))
            binding.tabKomoditas.setBackgroundResource(R.drawable.tab_inactive_bg)
            binding.tabKomoditas.setTextColor(ctx.getColor(R.color.text_secondary))

            binding.recyclerInvestasi.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerInvestasi.adapter = sahamAdapter
            sahamAdapter.submitList(InvestasiData.saham.toList())
            binding.btnRefresh.visibility = View.VISIBLE
            binding.sahamNote.text = getString(R.string.note_saham)
        }
    }

    private fun refreshSaham() {
        InvestasiData.saham.forEach { s ->
            val fluktuasi = (Random.nextDouble() - 0.5) * 2
            s.chg = String.format("%.2f", fluktuasi).toDouble()
            s.harga = (s.harga * (1 + fluktuasi / 100)).toInt()
        }
        sahamAdapter.submitList(InvestasiData.saham.toList())
        Toast.makeText(requireContext(), "Harga diperbarui (simulasi)", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
