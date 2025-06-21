package com.example.moora_decition_app.ui.assignment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moora_decition_app.data.db.MooraDatabase
import com.example.moora_decition_app.data.repository.AlternatifRepository
import com.example.moora_decition_app.data.repository.CriteriaDetailRepository
import com.example.moora_decition_app.data.repository.CriteriaRepository
import com.example.moora_decition_app.data.repository.EvaluationRepository
import com.example.moora_decition_app.model.Alternatif
import com.example.moora_decition_app.model.Criteria
import com.example.moora_decition_app.model.CriteriaDetail
import com.example.moora_decition_app.model.Evaluation
import com.example.moora_decition_app.model.EvaluationDisplay

class AssignmentViewModel(
    private val db: MooraDatabase
) : ViewModel() {

    private val alternatifRepo = AlternatifRepository(db)
    private val criteriaRepo = CriteriaRepository(db)
    private val detailRepo = CriteriaDetailRepository(db)
    private val evaluationRepo = EvaluationRepository(db)

    val alternatifList = MutableLiveData<List<Alternatif>>()
    val criteriaList = MutableLiveData<List<Criteria>>()
    val detailList = MutableLiveData<List<CriteriaDetail>>()
    val evaluationList = MutableLiveData<List<EvaluationDisplay>>()

    fun loadInitialData() {
        alternatifList.value = alternatifRepo.getAll()
        criteriaList.value = criteriaRepo.getAll()
        evaluationList.value = evaluationRepo.getGroupedEvaluation()
    }

    fun loadDetailByCriteria(code: String) {
        detailList.value = detailRepo.getByCriteriaCode(code)
    }

    fun addEvaluation(alternatifId: Int, detailId: Int) {
        evaluationRepo.create(Evaluation(0, alternatifId, detailId))
        evaluationList.value = evaluationRepo.getGroupedEvaluation() // ini seharusnya bekerja
    }
}
