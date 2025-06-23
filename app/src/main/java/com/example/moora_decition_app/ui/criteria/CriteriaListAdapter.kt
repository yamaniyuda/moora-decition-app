package com.example.moora_decition_app.ui.criteria

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moora_decition_app.R
import com.example.moora_decition_app.model.CriteriaWithDetails

class CriteriaListAdapter(private val list: List<CriteriaWithDetails>, private val onDeleteClick: (CriteriaWithDetails) -> Unit) :
    RecyclerView.Adapter<CriteriaListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val header: TextView = view.findViewById(R.id.textCriteriaHeader)
        val detailText: TextView = view.findViewById(R.id.textCriteriaDetails)
        val btnDelete: View = view.findViewById(R.id.btnDeleteCriteria)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_criteria_with_details, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.header.text = "${item.code} . ${item.name} . ${item.type} . ${item.weight}%"

        val detailText = item.details.joinToString("\n") {
            "- ${it.name} / ${it.level} / ${it.weight}"
        }
        holder.detailText.text = detailText

        holder.btnDelete.setOnClickListener {
            onDeleteClick(item)
        }
    }
}
