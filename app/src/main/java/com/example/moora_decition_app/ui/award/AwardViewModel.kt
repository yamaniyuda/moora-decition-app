package com.example.moora_decition_app.ui.award

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moora_decition_app.data.db.MooraDatabase
import com.example.moora_decition_app.data.repository.AwardRepository
import com.example.moora_decition_app.model.MooraScore

class AwardViewModel(private val db: MooraDatabase) : ViewModel() {

    private val awardRepo = AwardRepository(db)
    private val _scores = MutableLiveData<List<MooraScore>>()
    val scores: LiveData<List<MooraScore>> = _scores

    fun loadScores() {
        _scores.value = awardRepo.calculateMoora()
    }
}
