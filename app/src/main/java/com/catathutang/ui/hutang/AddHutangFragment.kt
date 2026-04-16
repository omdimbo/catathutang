package com.catathutang.ui.hutang

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.catathutang.data.model.Hutang
import com.catathutang.databinding.FragmentAddHutangBinding

class AddHutangFragment : Fragment() {

    private var _binding: FragmentAddHutangBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HutangViewModel by viewModels()
    private var selectedType = "h"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddHutangBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        binding.btnTypeBerhutang.setOnClickListener { setType("h") }
        binding.btnTypePiutang.setOnClickListener { setType("k") }
        setType("h")

        binding.btnSimpan.setOnClickListener { save() }
    }

    private fun setType(type: String) {
        selectedType = type
        val ctx = requireContext()
        if (type == "h") {
            binding.btnTypeBerhutang.setBackgroundColor(ctx.getColor(com.catathutang.R.color.red_light))
            binding.btnTypeBerhutang.setTextColor(ctx.getColor(com.catathutang.R.color.red_dark))
            binding.btnTypePiutang.setBackgroundColor(ctx.getColor(com.catathutang.R.color.bg_secondary))
            binding.btnTypePiutang.setTextColor(ctx.getColor(com.catathutang.R.color.text_secondary))
        } else {
            binding.btnTypePiutang.setBackgroundColor(ctx.getColor(com.catathutang.R.color.green_light))
            binding.btnTypePiutang.setTextColor(ctx.getColor(com.catathutang.R.color.green_dark))
            binding.btnTypeBerhutang.setBackgroundColor(ctx.getColor(com.catathutang.R.color.bg_secondary))
            binding.btnTypeBerhutang.setTextColor(ctx.getColor(com.catathutang.R.color.text_secondary))
        }
    }

    private fun save() {
        val nama = binding.etNama.text.toString().trim()
        val nominal = binding.etNominal.text.toString().toDoubleOrNull() ?: 0.0
        val ket = binding.etKeterangan.text.toString().trim()
        val kat = when (binding.spinnerKategori.selectedItemPosition) {
            0 -> "pribadi"
            1 -> "bisnis"
            else -> "keluarga"
        }

        if (nama.isEmpty() || nominal <= 0) {
            Toast.makeText(requireContext(), "Isi nama & nominal!", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.insert(Hutang(nama = nama, nominal = nominal, type = selectedType, keterangan = ket, kategori = kat))
        Toast.makeText(requireContext(), "Catatan tersimpan!", Toast.LENGTH_SHORT).show()
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
