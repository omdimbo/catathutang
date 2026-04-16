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
import com.catathutang.auth.UserSession
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

        adapter = HutangAdapter { hutang ->
            val action = HutangFragmentDirections.actionHutangToHutangDetail(hutang.id)
            findNavController().navigate(action)
        }
        binding.recyclerHutang.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerHutang.adapter = adapter

        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_hutang_to_addHutang)
        }

        // Show user avatar initials
        val session = UserSession(requireContext())
        session.userInfo?.let { user ->
            val initials = if (user.isGuest) "TM"
            else user.displayName.split(" ")
                .mapNotNull { it.firstOrNull()?.toString() }
                .take(2).joinToString("").uppercase()
            binding.tvUserAvatar.text = initials
        }

        observeViewModel()
        renderQuote()
        setupQuoteDots()
    }

    private fun observeViewModel() {
        viewModel.activeHutang.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.tvSubtitle.text = getString(R.string.hutang_active_count, list.size)
            binding.emptyState.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }
        viewModel.totalBerhutang.observe(viewLifecycleOwner) {
            binding.tvBerhutang.text = Formatter.fmt(it)
        }
        viewModel.totalPiutang.observe(viewLifecycleOwner) {
            binding.tvPiutang.text = Formatter.fmt(it)
        }
        viewModel.saldoNet.observe(viewLifecycleOwner) { net ->
            val color = if (net >= 0) requireContext().getColor(R.color.sage)
                        else requireContext().getColor(R.color.red_soft)
            binding.tvSaldo.setTextColor(color)
            binding.tvSaldo.text = (if (net >= 0) "+" else "-") + Formatter.fmt(abs(net))
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
                setOnClickListener { quoteIndex = index; renderQuote(); setupQuoteDots() }
            }
            binding.quoteDots.addView(dot)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
