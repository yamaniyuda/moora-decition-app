package com.example.moora_decition_app.model

data class CriteriaDetail(
    val id: Int,
    val criteriaCode: String,
    val name: String,
    val level: String,
    val weight: Double
)

data class CriteriaDetailInput(
    var name: String,
    var level: String,
    var weight: String
)