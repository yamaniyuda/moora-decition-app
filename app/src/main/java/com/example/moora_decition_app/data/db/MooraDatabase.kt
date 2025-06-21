package com.example.moora_decition_app.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MooraDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_ALTERNATIF)
        db.execSQL(CREATE_TABLE_CRITERIA)
        db.execSQL(CREATE_TABLE_CRITERIA_DETAIL)
        db.execSQL(CREATE_TABLE_EVALUATION)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop old tables
        db.execSQL("DROP TABLE IF EXISTS evaluation")
        db.execSQL("DROP TABLE IF EXISTS criteria_detail")
        db.execSQL("DROP TABLE IF EXISTS criteria")
        db.execSQL("DROP TABLE IF EXISTS alternatif")
        // Recreate
        onCreate(db)
    }

    companion object {
        const val DATABASE_NAME = "MooraDatabase.db"
        const val DATABASE_VERSION = 1

        private const val CREATE_TABLE_ALTERNATIF = """
            CREATE TABLE IF NOT EXISTS alternatif (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL
            )
        """

        private const val CREATE_TABLE_CRITERIA = """
            CREATE TABLE IF NOT EXISTS criteria (
                code TEXT PRIMARY KEY,
                name TEXT NOT NULL,
                weight REAL NOT NULL,
                type TEXT NOT NULL CHECK (type IN ('cost', 'benefit'))
            )
        """

        private const val CREATE_TABLE_CRITERIA_DETAIL = """
            CREATE TABLE IF NOT EXISTS criteria_detail (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                criteria_code TEXT NOT NULL,
                name TEXT NOT NULL,
                level TEXT NOT NULL,
                weight REAL NOT NULL,
                FOREIGN KEY(criteria_code) REFERENCES criteria(code) ON DELETE CASCADE
            )
        """

        private const val CREATE_TABLE_EVALUATION = """
            CREATE TABLE IF NOT EXISTS evaluation (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                alternatif_id INTEGER NOT NULL,
                criteria_detail_id INTEGER NOT NULL,
                FOREIGN KEY(alternatif_id) REFERENCES alternatif(id) ON DELETE CASCADE,
                FOREIGN KEY(criteria_detail_id) REFERENCES criteria_detail(id) ON DELETE CASCADE
            )
        """
    }
}
