package com.example.moora_decition_app.data.repository

import android.content.ContentValues
import com.example.moora_decition_app.data.db.MooraDatabase
import com.example.moora_decition_app.model.Evaluation
import com.example.moora_decition_app.model.EvaluationDetailDisplay
import com.example.moora_decition_app.model.EvaluationDisplay

class EvaluationRepository(private val db: MooraDatabase) {

    fun create(evaluation: Evaluation): Boolean {
        val values = ContentValues().apply {
            put("alternatif_id", evaluation.alternatifId)
            put("criteria_detail_id", evaluation.criteriaDetailId)
        }
        return db.writableDatabase.insert("evaluation", null, values) != -1L
    }

    fun getAll(): List<Evaluation> {
        val list = mutableListOf<Evaluation>()
        val cursor = db.readableDatabase.rawQuery("SELECT * FROM evaluation", null)
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    Evaluation(
                        id = cursor.getInt(0),
                        alternatifId = cursor.getInt(1),
                        criteriaDetailId = cursor.getInt(2)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun delete(id: Int): Boolean {
        return db.writableDatabase.delete("evaluation", "id = ?", arrayOf(id.toString())) > 0
    }

    fun getGroupedEvaluation(): List<EvaluationDisplay> {
        val map = mutableMapOf<String, MutableList<EvaluationDetailDisplay>>()

        val query = """
            SELECT 
                a.name AS alternatif_name,
                c.code AS criteria_code,
                c.name AS criteria_name,
                cd.name AS detail_name,
                cd.weight AS detail_weight
            FROM evaluation e
            JOIN alternatif a ON e.alternatif_id = a.id
            JOIN criteria_detail cd ON e.criteria_detail_id = cd.id
            JOIN criteria c ON cd.criteria_code = c.code
        """

        val cursor = db.readableDatabase.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val altName = cursor.getString(cursor.getColumnIndexOrThrow("alternatif_name"))
                val detail = EvaluationDetailDisplay(
                    criteriaCode = cursor.getString(cursor.getColumnIndexOrThrow("criteria_code")),
                    criteriaName = cursor.getString(cursor.getColumnIndexOrThrow("criteria_name")),
                    detailName = cursor.getString(cursor.getColumnIndexOrThrow("detail_name")),
                    detailWeight = cursor.getDouble(cursor.getColumnIndexOrThrow("detail_weight"))
                )
                if (!map.containsKey(altName)) {
                    map[altName] = mutableListOf()
                }
                map[altName]?.add(detail)
            } while (cursor.moveToNext())
        }
        cursor.close()

        return map.map { (altName, details) ->
            EvaluationDisplay(altName, details)
        }
    }

    fun deleteByAlternatifId(alternatifId: Int): Boolean {
        return db.writableDatabase.delete("evaluation", "alternatif_id = ?", arrayOf(alternatifId.toString())) > 0
    }
}
