package com.example.moora_decition_app.model

data class Evaluation(
    val id: Int,
    val alternatifId: Int,
    val criteriaDetailId: Int
)


data class EvaluationDisplay(
    val alternatifName: String,
    val details: List<EvaluationDetailDisplay>
)

data class EvaluationDetailDisplay(
    val criteriaCode: String,
    val criteriaName: String,
    val detailName: String,
    val detailWeight: Double
)