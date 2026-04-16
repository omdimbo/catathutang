package com.catathutang.ui.cicilan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.catathutang.R
import com.catathutang.databinding.FragmentCicilanBinding
import com.catathutang.utils.Formatter

class CicilanFragment : Fragment() {

    private var _binding: FragmentCicilanBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CicilanViewModel by viewModels()
    private lateinit var adapter: CicilanAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCicilanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CicilanAdapter { cicilan ->
            val action = CicilanFragmentDirections.actionCicilanToDetail(cicilan.id)
            findNavController().navigate(action)
        }

        binding.recyclerCicilan.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerCicilan.adapter = adapter

        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_cicilan_to_addCicilan)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.allCicilan.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.emptyState.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.totalHutang.observe(viewLifecycleOwner) {
            binding.tvTotalHutang.text = Formatter.fmt(it)
        }
        viewModel.totalPaid.observe(viewLifecycleOwner) {
            binding.tvTotalPaid.text = Formatter.fmt(it)
        }
        viewModel.totalSisa.observe(viewLifecycleOwner) {
            binding.tvTotalSisa.text = Formatter.fmt(it)
        }

        viewModel.activeCount.observe(viewLifecycleOwner) { active ->
            val lunas = viewModel.lunasCount.value ?: 0
            binding.tvSubtitle.text = "$active aktif · $lunas lunas"
        }
        viewModel.lunasCount.observe(viewLifecycleOwner) { lunas ->
            val active = viewModel.activeCount.value ?: 0
            binding.tvSubtitle.text = "$active aktif · $lunas lunas"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
