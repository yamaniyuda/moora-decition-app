package com.example.moora_decition_app.ui.assignment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moora_decition_app.data.db.MooraDatabase
import com.example.moora_decition_app.data.repository.AlternatifRepository
import com.example.moora_decition_app.ui.home.HomeViewModel

class AsignmentViewModelFactory(private val db: MooraDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AssignmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AssignmentViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
