package com.catathutang.ui.hutang

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.catathutang.R
import com.catathutang.databinding.FragmentHutangBinding
import com.catathutang.utils.Formatter
import com.catathutang.utils.quotes
import kotlin.math.abs

class HutangFragment : Fragment() {

    private var _binding: FragmentHutangBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HutangViewModel by viewModels()
    private lateinit var adapter: HutangAdapter
    private var quoteIndex = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHutangBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = HutangAdapter(
            onItemClick = { hutang ->
                val action = HutangFragmentDirections.actionHutangToHutangDetail(hutang.id)
                findNavController().navigate(action)
            }
        )

        binding.recyclerHutang.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerHutang.adapter = adapter

        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_hutang_to_addHutang)
        }

        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_hutang_to_addHutang)
        }

        observeViewModel()
        renderQuote()
        setupQuoteDots()
    }

    private fun observeViewModel() {
        viewModel.activeHutang.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.tvSubtitle.text = "${list.size} catatan aktif"
            binding.emptyState.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.totalBerhutang.observe(viewLifecycleOwner) { total ->
            binding.tvBerhutang.text = Formatter.fmt(total)
        }

        viewModel.totalPiutang.observe(viewLifecycleOwner) { total ->
            binding.tvPiutang.text = Formatter.fmt(total)
        }

        viewModel.saldoNet.observe(viewLifecycleOwner) { net ->
            val color = if (net >= 0) requireContext().getColor(R.color.green)
            else requireContext().getColor(R.color.red)
            binding.tvSaldo.setTextColor(color)
            val prefix = if (net >= 0) "+" else "-"
            binding.tvSaldo.text = prefix + Formatter.fmt(abs(net))
        }
    }

    private fun renderQuote() {
        val q = quotes[quoteIndex]
        binding.tvQuote.text = "\"${q.first}\""
        binding.tvQuoteAuthor.text = q.second
    }

    private fun setupQuoteDots() {
        binding.quoteDots.removeAllViews()
        quotes.forEachIndexed { index, _ ->
            val dot = View(requireContext()).apply {
                val size = resources.getDimensionPixelSize(R.dimen.quote_dot_size)
                layoutParams = ViewGroup.MarginLayoutParams(size, size).apply { marginEnd = 8 }
                background = requireContext().getDrawable(
                    if (index == quoteIndex) R.drawable.dot_active else R.drawable.dot_inactive
                )
                setOnClickListener {
                    quoteIndex = index
                    renderQuote()
                    setupQuoteDots()
                }
            }
            binding.quoteDots.addView(dot)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
