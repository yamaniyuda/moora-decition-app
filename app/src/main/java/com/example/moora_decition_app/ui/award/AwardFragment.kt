package com.example.moora_decition_app.ui.award

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moora_decition_app.data.db.MooraDatabase
import com.example.moora_decition_app.databinding.FragmentAwardBinding

class AwardFragment : Fragment() {

    private var _binding: FragmentAwardBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AwardViewModel
    private lateinit var adapter: AwardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAwardBinding.inflate(inflater, container, false)
        val db = MooraDatabase(requireContext())
        val factory = AwardViewModelFactory(db)
        viewModel = ViewModelProvider(this, factory)[AwardViewModel::class.java]

        adapter = AwardAdapter()
        binding.recyclerViewAward.adapter = adapter
        binding.recyclerViewAward.layoutManager = LinearLayoutManager(requireContext())

        viewModel.scores.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.loadScores()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
