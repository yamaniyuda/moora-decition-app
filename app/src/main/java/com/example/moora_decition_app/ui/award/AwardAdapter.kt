package com.example.moora_decition_app.ui.award

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moora_decition_app.R
import com.example.moora_decition_app.model.MooraScore

class AwardAdapter : RecyclerView.Adapter<AwardAdapter.ViewHolder>() {

    private val data = mutableListOf<MooraScore>()

    fun submitList(newData: List<MooraScore>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textNama: TextView = view.findViewById(R.id.textNamaAlternatif)
        val textScore: TextView = view.findViewById(R.id.textScore)

        fun bind(item: MooraScore) {
            textNama.text = item.alternatifName
            textScore.text = "Skor: %.4f".format(item.score)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_award, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = data.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }
}
