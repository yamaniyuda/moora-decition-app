package com.example.moora_decition_app.ui.criteria

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moora_decition_app.R
import com.example.moora_decition_app.data.db.MooraDatabase
import com.example.moora_decition_app.data.repository.CriteriaDetailRepository
import com.example.moora_decition_app.data.repository.CriteriaRepository
import com.example.moora_decition_app.databinding.FragmentAddCriteriaBinding
import com.example.moora_decition_app.model.Criteria
import com.example.moora_decition_app.model.CriteriaDetail
import com.example.moora_decition_app.model.CriteriaDetailInput

class AddCriteriaFragment : Fragment() {

    private var _binding: FragmentAddCriteriaBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: MooraDatabase
    private lateinit var criteriaRepo: CriteriaRepository
    private lateinit var detailRepo: CriteriaDetailRepository
    private lateinit var adapter: DetailCriteriaAdapter
    private val detailList = mutableListOf<CriteriaDetailInput>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCriteriaBinding.inflate(inflater, container, false)

        // Init DB & Repo
        db = MooraDatabase(requireContext())
        criteriaRepo = CriteriaRepository(db)
        detailRepo = CriteriaDetailRepository(db)

        // Set adapter untuk detail kriteria
        adapter = DetailCriteriaAdapter(detailList) { position ->
            detailList.removeAt(position)
            adapter.notifyItemRemoved(position)
        }
        binding.recyclerViewDetail.adapter = adapter
        binding.recyclerViewDetail.layoutManager = LinearLayoutManager(requireContext())

        // Tambah item detail
        binding.btnTambahDetail.setOnClickListener {
            detailList.add(CriteriaDetailInput("", "", ""))
            adapter.notifyItemInserted(detailList.size - 1)
        }

        // Simpan semua data
        binding.btnSimpan.setOnClickListener {
            saveCriteria()
        }

        return binding.root
    }

    private fun saveCriteria() {
        val kode = binding.inputKode.text.toString().trim()
        val nama = binding.inputNama.text.toString().trim()
        val bobot = binding.inputBobot.text.toString().toDoubleOrNull()
        val jenis = when (binding.radioGroupJenis.checkedRadioButtonId) {
            R.id.radioBenefit -> "benefit"
            R.id.radioCost -> "cost"
            else -> ""
        }

        if (kode.isEmpty() || nama.isEmpty() || bobot == null || jenis.isEmpty()) {
            Toast.makeText(requireContext(), "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        val criteria = Criteria(kode, nama, bobot, jenis)

        if (criteriaRepo.create(criteria)) {
            detailList.forEach { input ->
                val detail = CriteriaDetail(
                    id = 0,
                    criteriaCode = kode,
                    level = input.level,
                    name = input.name,
                    weight = input.weight.toDoubleOrNull() ?: 0.0
                )
                detailRepo.create(detail)
            }

            Toast.makeText(requireContext(), "Berhasil disimpan", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        } else {
            Toast.makeText(requireContext(), "Gagal simpan!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
