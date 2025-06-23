package com.example.moora_decition_app.data.repository

import com.example.moora_decition_app.data.db.MooraDatabase
import com.example.moora_decition_app.model.MooraScore

class AwardRepository(private val db: MooraDatabase) {

    fun calculateMoora(): List<MooraScore> {
        val alternatifList = mutableSetOf<String>()
        val criteriaList = mutableSetOf<String>()
        val weightMap = mutableMapOf<String, Double>()
        val typeMap = mutableMapOf<String, String>()
        val valueMap = mutableMapOf<Pair<String, String>, Double>()

        // Step 1: Ambil bobot dan tipe kriteria
        val critCursor = db.readableDatabase.rawQuery("SELECT code, weight, type FROM criteria", null)
        if (critCursor.moveToFirst()) {
            do {
                val code = critCursor.getString(0)
                val weight = critCursor.getDouble(1)
                val type = critCursor.getString(2)

                criteriaList.add(code)
                weightMap[code] = weight
                typeMap[code] = type
            } while (critCursor.moveToNext())
        }
        critCursor.close()

        // Step 2: Ambil nilai evaluasi
        val evalCursor = db.readableDatabase.rawQuery("""
        SELECT 
            a.name AS alt_name,
            c.code AS crit_code,
            cd.weight AS detail_weight
        FROM evaluation e
        JOIN alternatif a ON a.id = e.alternatif_id
        JOIN criteria_detail cd ON cd.id = e.criteria_detail_id
        JOIN criteria c ON c.code = cd.criteria_code
    """, null)

        if (evalCursor.moveToFirst()) {
            do {
                val alt = evalCursor.getString(0)
                val crit = evalCursor.getString(1)
                val weight = evalCursor.getDouble(2)

                alternatifList.add(alt)
                valueMap[Pair(alt, crit)] = weight
            } while (evalCursor.moveToNext())
        }
        evalCursor.close()

        // Step 3: Normalisasi pakai akar jumlah kuadrat per kriteria
        val normDivisor = mutableMapOf<String, Double>()
        for (crit in criteriaList) {
            var sumSquare = 0.0
            for (alt in alternatifList) {
                val x = valueMap[Pair(alt, crit)] ?: 0.0
                sumSquare += x * x
            }
            normDivisor[crit] = Math.sqrt(sumSquare)
        }

        // Step 4: Hitung skor Y
        val result = mutableListOf<MooraScore>()
        for (alt in alternatifList) {
            var y = 0.0
            for (crit in criteriaList) {
                val x = valueMap[Pair(alt, crit)] ?: 0.0
                val norm = if (normDivisor[crit] != 0.0) x / normDivisor[crit]!! else 0.0
                val w = weightMap[crit] ?: 0.0

                if (typeMap[crit] == "benefit") {
                    y += norm * w
                } else {
                    y -= norm * w
                }
            }
            result.add(MooraScore(alt, y))
        }

        return result.sortedByDescending { it.score }
    }
}
