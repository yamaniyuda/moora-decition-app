package com.example.moora_decition_app.ui.assignment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moora_decition_app.R
import com.example.moora_decition_app.model.EvaluationDisplay

class EvaluationAdapter : RecyclerView.Adapter<EvaluationAdapter.ViewHolder>() {

    private val data = mutableListOf<EvaluationDisplay>()

    fun submitList(newData: List<EvaluationDisplay>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val textNama: TextView = view.findViewById(R.id.textNamaAlternatif)
        private val listContainer: LinearLayout = view.findViewById(R.id.listDetail)

        fun bind(evaluation: EvaluationDisplay) {
            textNama.text = evaluation.alternatifName
            listContainer.removeAllViews()

            evaluation.details.forEach {
                val item = TextView(view.context)
                item.text = "- ${it.criteriaName} (${it.criteriaCode}) : ${it.detailWeight}"
                listContainer.addView(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_assignment, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }
}
