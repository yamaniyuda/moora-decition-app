package com.example.moora_decition_app.data.repository

import android.content.ContentValues
import com.example.moora_decition_app.data.db.MooraDatabase
import com.example.moora_decition_app.model.CriteriaDetail


class CriteriaDetailRepository(private val db: MooraDatabase) {


    fun create(detail: CriteriaDetail): Boolean {
        val values = ContentValues().apply {
            put("criteria_code", detail.criteriaCode)
            put("name", detail.name)
            put("level", detail.level)
            put("weight", detail.weight)
        }
        return db.writableDatabase.insert("criteria_detail", null, values) != -1L
    }

    fun getByCriteriaCode(code: String): List<CriteriaDetail> {
        val list = mutableListOf<CriteriaDetail>()
        val cursor = db.readableDatabase.rawQuery(
            "SELECT * FROM criteria_detail WHERE criteria_code = ?",
            arrayOf(code)
        )
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    CriteriaDetail(
                        id = cursor.getInt(0),
                        criteriaCode = cursor.getString(1),
                        name = cursor.getString(2),
                        level = cursor.getString(3),
                        weight = cursor.getDouble(4)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun update(detail: CriteriaDetail): Boolean {
        val values = ContentValues().apply {
            put("criteria_code", detail.criteriaCode)
            put("level", detail.level)
            put("weight", detail.weight)
        }
        return db.writableDatabase.update("criteria_detail", values, "id = ?", arrayOf(detail.id.toString())) > 0
    }

    fun delete(id: Int): Boolean {
        return db.writableDatabase.delete("criteria_detail", "id = ?", arrayOf(id.toString())) > 0
    }
}
