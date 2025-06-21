package com.example.moora_decition_app.data.repository

import android.content.ContentValues
import com.example.moora_decition_app.data.db.MooraDatabase
import com.example.moora_decition_app.model.Alternatif

class AlternatifRepository(private val db: MooraDatabase) {

    fun create(name: String): Boolean {
        val values = ContentValues().apply { put("name", name) }
        return db.writableDatabase.insert("alternatif", null, values) != -1L
    }

    fun getAll(): List<Alternatif> {
        val list = mutableListOf<Alternatif>()
        val cursor = db.readableDatabase.rawQuery("SELECT * FROM alternatif", null)
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    Alternatif(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun update(id: Int, name: String): Boolean {
        val values = ContentValues().apply { put("name", name) }
        return db.writableDatabase.update("alternatif", values, "id = ?", arrayOf(id.toString())) > 0
    }

    fun delete(id: Int): Boolean {
        return db.writableDatabase.delete("alternatif", "id = ?", arrayOf(id.toString())) > 0
    }
}
