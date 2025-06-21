package com.example.moora_decition_app.ui.assignment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moora_decition_app.data.db.MooraDatabase
import com.example.moora_decition_app.databinding.FragmentAssigmentBinding

class AssignmentFragment : Fragment() {

    private var _binding: FragmentAssigmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AssignmentViewModel
    private lateinit var db: MooraDatabase
    private lateinit var adapter: EvaluationAdapter

    private var selectedAlternatifId: Int? = null
    private var selectedCriteriaCode: String? = null
    private var selectedDetailId: Int? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAssigmentBinding.inflate(inflater, container, false)
        db = MooraDatabase(requireContext())
        val factory = AsignmentViewModelFactory(db)
        viewModel = ViewModelProvider(this, factory)[AssignmentViewModel::class.java]

        adapter = EvaluationAdapter()
        binding.recyclerViewAssignment.adapter = adapter
        binding.recyclerViewAssignment.layoutManager = LinearLayoutManager(requireContext())

        setupObservers()
        setupSpinners()
        setupListeners()

        viewModel.loadInitialData()

        return binding.root
    }

    private fun setupObservers() {
        viewModel.alternatifList.observe(viewLifecycleOwner) {
            binding.spinnerAlternatif.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                it.map { a -> a.name }
            )
        }

        viewModel.criteriaList.observe(viewLifecycleOwner) {
            binding.spinnerKriteria.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                it.map { c -> c.name }
            )
        }

        viewModel.detailList.observe(viewLifecycleOwner) {
            binding.spinnerDetail.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                it.map { d -> d.name }
            )
        }

        viewModel.evaluationList.observe(viewLifecycleOwner) {
            Log.d("AssignmentFragment", "evaluationList updated: ${it.size}")
            adapter.submitList(it)
        }
    }

    private fun setupSpinners() {
        binding.spinnerAlternatif.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedAlternatifId = viewModel.alternatifList.value?.get(position)?.id
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        binding.spinnerKriteria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCriteriaCode = viewModel.criteriaList.value?.get(position)?.code
                selectedCriteriaCode?.let { viewModel.loadDetailByCriteria(it) }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        binding.spinnerDetail.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedDetailId = viewModel.detailList.value?.get(position)?.id
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun setupListeners() {
        binding.btnTambahAssignment.setOnClickListener {
            val altId = selectedAlternatifId
            val detailId = selectedDetailId

            if (altId != null && detailId != null) {
                viewModel.addEvaluation(altId, detailId)
            } else {
                Toast.makeText(requireContext(), "Semua pilihan harus dipilih!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
