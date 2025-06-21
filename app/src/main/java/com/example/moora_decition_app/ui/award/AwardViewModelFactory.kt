package com.example.moora_decition_app.ui.award

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moora_decition_app.data.db.MooraDatabase

class AwardViewModelFactory(private val db: MooraDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AwardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AwardViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
