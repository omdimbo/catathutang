package com.catathutang.ui.investasi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.catathutang.R
import com.catathutang.databinding.FragmentInvestasiBinding

class InvestasiFragment : Fragment() {

    private var _binding: FragmentInvestasiBinding? = null
    private val binding get() = _binding!!
    private val viewModel: InvestasiViewModel by viewModels()

    private lateinit var komoditasAdapter: KomoditasAdapter
    private lateinit var sahamAdapter: SahamAdapter
    private var currentTab = "komoditas"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInvestasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        komoditasAdapter = KomoditasAdapter()
        sahamAdapter = SahamAdapter()

        binding.tabKomoditas.setOnClickListener { switchTab("komoditas") }
        binding.tabSaham.setOnClickListener { switchTab("saham") }
        binding.btnRefresh.setOnClickListener {
            viewModel.refreshAll()
            Toast.makeText(requireContext(), "Memperbarui data...", Toast.LENGTH_SHORT).show()
        }

        observeViewModel()
        viewModel.loadKomoditas()
        viewModel.loadSaham()
        viewModel.startAutoRefresh(60_000L)
        switchTab("komoditas")
    }

    private fun observeViewModel() {
        viewModel.komoditasState.observe(viewLifecycleOwner) { state ->
            if (currentTab != "komoditas") return@observe
            when (state) {
                is UiState.Loading -> showLoading(true)
                is UiState.Success -> {
                    showLoading(false)
                    komoditasAdapter.submitList(state.data)
                }
                is UiState.Error -> {
                    showLoading(false)
                    binding.sahamNote.text = "⚠️ ${state.message}. Cek koneksi internet."
                }
            }
        }

        viewModel.sahamState.observe(viewLifecycleOwner) { state ->
            if (currentTab != "saham") return@observe
            when (state) {
                is UiState.Loading -> showLoading(true)
                is UiState.Success -> {
                    showLoading(false)
                    sahamAdapter.submitList(state.data)
                }
                is UiState.Error -> {
                    showLoading(false)
                    binding.sahamNote.text = "⚠️ ${state.message}. Cek koneksi internet."
                }
            }
        }

        viewModel.lastUpdated.observe(viewLifecycleOwner) { timestamp ->
            binding.tvLastUpdated.text = timestamp
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.recyclerInvestasi.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun switchTab(tab: String) {
        currentTab = tab
        val ctx = requireContext()

        if (tab == "komoditas") {
            binding.tabKomoditas.setBackgroundResource(R.drawable.tab_active_bg)
            binding.tabKomoditas.setTextColor(ctx.getColor(R.color.white))
            binding.tabSaham.setBackgroundResource(R.drawable.tab_inactive_bg)
            binding.tabSaham.setTextColor(ctx.getColor(R.color.text_secondary))
            binding.recyclerInvestasi.layoutManager = GridLayoutManager(ctx, 2)
            binding.recyclerInvestasi.adapter = komoditasAdapter
            when (val s = viewModel.komoditasState.value) {
                is UiState.Success -> { showLoading(false); komoditasAdapter.submitList(s.data) }
                else -> showLoading(true)
            }
            binding.sahamNote.text = getString(R.string.note_komoditas)
        } else {
            binding.tabSaham.setBackgroundResource(R.drawable.tab_active_bg)
            binding.tabSaham.setTextColor(ctx.getColor(R.color.white))
            binding.tabKomoditas.setBackgroundResource(R.drawable.tab_inactive_bg)
            binding.tabKomoditas.setTextColor(ctx.getColor(R.color.text_secondary))
            binding.recyclerInvestasi.layoutManager = LinearLayoutManager(ctx)
            binding.recyclerInvestasi.adapter = sahamAdapter
            when (val s = viewModel.sahamState.value) {
                is UiState.Success -> { showLoading(false); sahamAdapter.submitList(s.data) }
                else -> showLoading(true)
            }
            binding.sahamNote.text = getString(R.string.note_saham_realtime)
        }
        binding.btnRefresh.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
