package com.example.moora_decition_app.ui.criteria

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.moora_decition_app.R
import com.example.moora_decition_app.model.CriteriaDetailInput

class DetailCriteriaAdapter(
    private val detailList: MutableList<CriteriaDetailInput>,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<DetailCriteriaAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val inputName: EditText = view.findViewById(R.id.input_criteria_detail_name)
        private val inputLevel: EditText = view.findViewById(R.id.input_criteria_detail_level)
        private val inputWeight: EditText = view.findViewById(R.id.input_criteria_detail_weight)
        private val btnHapus: ImageButton = view.findViewById(R.id.btnHapus)

        fun bind(detail: CriteriaDetailInput) {
            inputName.setText(detail.name)
            inputLevel.setText(detail.level)
            inputWeight.setText(detail.weight)

            // Update data saat user input
            inputName.doAfterTextChanged { detail.name = it.toString() }
            inputLevel.doAfterTextChanged { detail.level = it.toString() }
            inputWeight.doAfterTextChanged { detail.weight = it.toString() }

            // Hapus item saat tombol diklik
            btnHapus.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    onDeleteClick(pos)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_detail_kriteria_input, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(detailList[position])
    }

    override fun getItemCount(): Int = detailList.size
}
