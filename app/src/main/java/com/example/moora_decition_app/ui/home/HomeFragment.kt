package com.example.moora_decition_app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moora_decition_app.data.db.MooraDatabase
import com.example.moora_decition_app.data.repository.AlternatifRepository
import com.example.moora_decition_app.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var adapter: ListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root = binding.root

        // Init database & repository
        val database = MooraDatabase(requireContext())
        val repository = AlternatifRepository(database)
        val factory = HomeViewModelFactory(repository)
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        // Adapter
        adapter = ListAdapter(listOf(), onDelete = { alternative ->
            homeViewModel.deleteItem(alternative.id)
        })
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        homeViewModel.items.observe(viewLifecycleOwner) { list ->
            adapter.updateData(list)
        }

        // Tambah data
        binding.btnAdd.setOnClickListener {
            val text = binding.editInput.text.toString().trim()
            if (text.isNotEmpty()) {
                homeViewModel.addItem(text)
                binding.editInput.setText("")
                binding.editInput.clearFocus()
            }
        }

        return root
    }


    private fun dismissKeyboard() {
        val imm = requireContext().getSystemService(InputMethodManager::class.java)
        imm.hideSoftInputFromWindow(binding.editInput.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
