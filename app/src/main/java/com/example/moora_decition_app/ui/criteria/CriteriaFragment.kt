package com.example.moora_decition_app.ui.criteria

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moora_decition_app.R
import com.example.moora_decition_app.data.db.MooraDatabase
import com.example.moora_decition_app.data.repository.CriteriaRepository
import com.example.moora_decition_app.databinding.FragmentCriteriaBinding
import com.example.moora_decition_app.model.CriteriaWithDetails

class CriteriaFragment : Fragment() {

    private var _binding: FragmentCriteriaBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: MooraDatabase
    private lateinit var criteriaRepo: CriteriaRepository
    private lateinit var adapter: CriteriaListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCriteriaBinding.inflate(inflater, container, false)

        // DB & Repo
        db = MooraDatabase(requireContext())
        criteriaRepo = CriteriaRepository(db)

        // Setup RecyclerView
        val data = criteriaRepo.getAllWithDetails()
        adapter = CriteriaListAdapter(data) { selectedItem ->
            criteriaRepo.deleteByCode(selectedItem.code)
            val updatedList = criteriaRepo.getAllWithDetails()
            adapter = CriteriaListAdapter(updatedList, this@CriteriaFragment::onDeleteClicked)
            binding.recyclerViewCriteria.adapter = adapter
        }
        binding.recyclerViewCriteria.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCriteria.adapter = adapter

        // Tombol tambah navigasi
        binding.btnGoToAddKriteria.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_criteria_to_navigation_add_criteria)
        }

        return binding.root
    }

    private fun onDeleteClicked(item: CriteriaWithDetails) {
        criteriaRepo.deleteByCode(item.code)
        val updatedList = criteriaRepo.getAllWithDetails()
        adapter = CriteriaListAdapter(updatedList, ::onDeleteClicked)
        binding.recyclerViewCriteria.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
