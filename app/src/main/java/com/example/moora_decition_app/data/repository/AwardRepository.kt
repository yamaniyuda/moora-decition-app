package com.example.moora_decition_app.data.repository

import com.example.moora_decition_app.data.db.MooraDatabase
import com.example.moora_decition_app.model.MooraScore

class AwardRepository(private val db: MooraDatabase) {

    fun calculateMoora(): List<MooraScore> {
        val altMap = mutableMapOf<String, MutableMap<String, Double>>()
        val weightMap = mutableMapOf<String, Double>()
        val typeMap = mutableMapOf<String, String>()
        val maxPerCriteria = mutableMapOf<String, Double>()

        // Ambil bobot & jenis kriteria
        val criteriaCursor = db.readableDatabase.rawQuery("SELECT code, weight, type FROM criteria", null)
        if (criteriaCursor.moveToFirst()) {
            do {
                val code = criteriaCursor.getString(0)
                val weight = criteriaCursor.getDouble(1)
                val type = criteriaCursor.getString(2)
                weightMap[code] = weight
                typeMap[code] = type
                maxPerCriteria[code] = 0.0
            } while (criteriaCursor.moveToNext())
        }
        criteriaCursor.close()

        val query = """
            SELECT 
                a.name AS alt_name,
                c.code AS crit_code,
                cd.weight AS detail_weight
            FROM evaluation e
            JOIN alternatif a ON a.id = e.alternatif_id
            JOIN criteria_detail cd ON cd.id = e.criteria_detail_id
            JOIN criteria c ON c.code = cd.criteria_code
        """.trimIndent()

        val cursor = db.readableDatabase.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val altName = cursor.getString(0)
                val critCode = cursor.getString(1)
                val detailWeight = cursor.getDouble(2)

                val critMap = altMap.getOrPut(altName) { mutableMapOf() }
                critMap[critCode] = detailWeight

                // Simpan max untuk normalisasi
                maxPerCriteria[critCode] = maxOf(maxPerCriteria[critCode] ?: 0.0, detailWeight)
            } while (cursor.moveToNext())
        }
        cursor.close()

        val result = mutableListOf<MooraScore>()

        // Hitung skor
        for ((alt, critValues) in altMap) {
            var score = 0.0

            for ((crit, _) in weightMap) {
                val rawValue = critValues[crit] ?: 0.0
                val max = maxPerCriteria[crit] ?: 1.0
                val normalized = if (max != 0.0) rawValue / max else 0.0
                val weighted = normalized * (weightMap[crit] ?: 0.0)

                score += if (typeMap[crit] == "benefit") weighted else -weighted
            }

            result.add(MooraScore(alt, score))
        }

        return result.sortedByDescending { it.score }
    }
}
