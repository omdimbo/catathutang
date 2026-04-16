package com.catathutang.ui.hutang

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
import com.catathutang.databinding.FragmentHutangDetailBinding
import com.catathutang.utils.AvatarHelper
import com.catathutang.utils.Formatter

class HutangDetailFragment : Fragment() {

    private var _binding: FragmentHutangDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HutangViewModel by viewModels()
    private val args: HutangDetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHutangDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        viewModel.allHutang.observe(viewLifecycleOwner) { list ->
            val hutang = list.find { it.id == args.hutangId } ?: return@observe

            binding.tvAvatar.text = AvatarHelper.initials(hutang.nama)
            binding.tvAvatar.setBackgroundColor(AvatarHelper.bgColor(hutang.nama))
            binding.tvAvatar.setTextColor(AvatarHelper.textColor(hutang.nama))
            binding.tvName.text = hutang.nama
            binding.tvKeterangan.text = if (hutang.keterangan.isNotEmpty()) hutang.keterangan else "—"
            binding.tvKategori.text = hutang.kategori.replaceFirstChar { it.uppercase() }
            val typeLabel = if (hutang.type == "h") "Saya berhutang" else "Saya berpiutang"
            binding.tvType.text = typeLabel
            val color = if (hutang.type == "h") requireContext().getColor(com.catathutang.R.color.red)
                        else requireContext().getColor(com.catathutang.R.color.green)
            binding.tvNominal.setTextColor(color)
            val prefix = if (hutang.type == "h") "- " else "+ "
            binding.tvNominal.text = "$prefix${Formatter.fmtFull(hutang.nominal)}"

            binding.btnLunas.setOnClickListener {
                AlertDialog.Builder(requireContext())
                    .setTitle("Tandai Lunas")
                    .setMessage("Tandai \"${hutang.nama}\" sebagai lunas?")
                    .setPositiveButton("Ya") { _, _ ->
                        viewModel.markLunas(hutang)
                        Toast.makeText(requireContext(), "Ditandai lunas!", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            }

            binding.btnHapus.setOnClickListener {
                AlertDialog.Builder(requireContext())
                    .setTitle("Hapus Catatan")
                    .setMessage("Hapus catatan \"${hutang.nama}\"?")
                    .setPositiveButton("Hapus") { _, _ ->
                        viewModel.delete(hutang)
                        Toast.makeText(requireContext(), "Catatan dihapus", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
