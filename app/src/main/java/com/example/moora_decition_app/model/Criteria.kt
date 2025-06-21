package com.example.moora_decition_app.model

data class Criteria(
    val code: String,
    val name: String,
    val weight: Double,
    val type: String // "cost" or "benefit"
)

data class CriteriaWithDetails(
    val code: String,
    val name: String,
    val type: String,
    val weight: Double,
    val details: List<CriteriaDetail>
)