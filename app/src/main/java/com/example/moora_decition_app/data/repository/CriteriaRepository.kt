package com.example.moora_decition_app.data.repository

import android.content.ContentValues
import com.example.moora_decition_app.data.db.MooraDatabase
import com.example.moora_decition_app.model.Criteria
import com.example.moora_decition_app.model.CriteriaDetail
import com.example.moora_decition_app.model.CriteriaWithDetails

class CriteriaRepository(private val db: MooraDatabase) {

    fun create(criteria: Criteria): Boolean {
        val values = ContentValues().apply {
            put("code", criteria.code)
            put("name", criteria.name)
            put("weight", criteria.weight)
            put("type", criteria.type.lowercase())
        }
        return db.writableDatabase.insert("criteria", null, values) != -1L
    }

    fun getAll(): List<Criteria> {
        val list = mutableListOf<Criteria>()
        val cursor = db.readableDatabase.rawQuery("SELECT * FROM criteria", null)
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    Criteria(
                        code = cursor.getString(0),
                        name = cursor.getString(1),
                        weight = cursor.getDouble(2),
                        type = cursor.getString(3)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun update(criteria: Criteria): Boolean {
        val values = ContentValues().apply {
            put("name", criteria.name)
            put("weight", criteria.weight)
            put("type", criteria.type.lowercase())
        }
        return db.writableDatabase.update("criteria", values, "code = ?", arrayOf(criteria.code)) > 0
    }

    fun delete(code: String): Boolean {
        return db.writableDatabase.delete("criteria", "code = ?", arrayOf(code)) > 0
    }


    fun getAllWithDetails(): List<CriteriaWithDetails> {
        val list = mutableListOf<CriteriaWithDetails>()
        val db = db.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM criteria", null)

        if (cursor.moveToFirst()) {
            do {
                val code = cursor.getString(cursor.getColumnIndexOrThrow("code"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val weight = cursor.getDouble(cursor.getColumnIndexOrThrow("weight"))
                val type = cursor.getString(cursor.getColumnIndexOrThrow("type"))

                val detailCursor = db.rawQuery(
                    "SELECT * FROM criteria_detail WHERE criteria_code = ?",
                    arrayOf(code)
                )

                val details = mutableListOf<CriteriaDetail>()
                if (detailCursor.moveToFirst()) {
                    do {
                        details.add(
                            CriteriaDetail(
                                id = detailCursor.getInt(detailCursor.getColumnIndexOrThrow("id")),
                                criteriaCode = code,
                                level = detailCursor.getString(detailCursor.getColumnIndexOrThrow("level")),
                                name = detailCursor.getString(detailCursor.getColumnIndexOrThrow("name")),
                                weight = detailCursor.getDouble(detailCursor.getColumnIndexOrThrow("weight"))
                            )
                        )
                    } while (detailCursor.moveToNext())
                }
                detailCursor.close()

                list.add(CriteriaWithDetails(code, name, type, weight, details))
            } while (cursor.moveToNext())
        }
        cursor.close()

        return list
    }
}
