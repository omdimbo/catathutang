package com.catathutang.ui.cicilan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.catathutang.R
import com.catathutang.data.model.Cicilan
import com.catathutang.databinding.FragmentAddCicilanBinding
import kotlin.math.roundToInt

class AddCicilanFragment : Fragment() {

    private var _binding: FragmentAddCicilanBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CicilanViewModel by viewModels()
    private var selectedKat = "hp"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCicilanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener { findNavController().popBackStack() }
        setupCategoryButtons()
        binding.btnSimpan.setOnClickListener { save() }
    }

    private fun setupCategoryButtons() {
        val cats = listOf(
            binding.catHp to "hp",
            binding.catMotor to "motor",
            binding.catBarang to "barang",
            binding.catLainnya to "lainnya"
        )
        cats.forEach { (btn, key) ->
            btn.setOnClickListener {
                selectedKat = key
                highlightCat(cats)
            }
        }
        highlightCat(cats)
    }

    private fun highlightCat(cats: List<Pair<LinearLayout, String>>) {
        val ctx = requireContext()
        cats.forEach { (btn, key) ->
            val label = btn.getChildAt(1) as? TextView ?: return@forEach
            if (key == selectedKat) {
                btn.setBackgroundColor(ctx.getColor(R.color.red_light))
                label.setTextColor(ctx.getColor(R.color.red_dark))
            } else {
                btn.setBackgroundResource(R.drawable.cat_btn_bg)
                label.setTextColor(ctx.getColor(R.color.text_secondary))
            }
        }
    }

    private fun save() {
        val nama = binding.etNama.text.toString().trim()
        val total = binding.etTotal.text.toString().toDoubleOrNull() ?: 0.0
        val cic = binding.etCicilan.text.toString().toDoubleOrNull() ?: 0.0
        val tenor = binding.etTenor.text.toString().toIntOrNull() ?: 12
        val dari = binding.etDari.text.toString().trim()

        if (nama.isEmpty() || total <= 0) {
            Toast.makeText(requireContext(), "Isi nama & total!", Toast.LENGTH_SHORT).show()
            return
        }

        val perBulan = if (cic > 0) cic else (total / tenor).roundToInt().toDouble()
        viewModel.insert(
            Cicilan(
                nama = nama,
                kategori = selectedKat,
                total = total,
                perBulan = perBulan,
                tenor = tenor,
                dari = dari
            )
        )
        Toast.makeText(requireContext(), "Cicilan ditambahkan!", Toast.LENGTH_SHORT).show()
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
