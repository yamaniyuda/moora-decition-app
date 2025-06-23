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
        db.execSQL(DATA_TABLE_ALTERNATIF)
        db.execSQL(DATA_TABLE_CRITERIA)
        db.execSQL(DATA_TABLE_CRITERIA_DETAIL)
        db.execSQL(DATA_TABLE_EVALUATION)
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

        private const val DATA_TABLE_ALTERNATIF = """
            INSERT INTO alternatif (name) VALUES
            ('Alternatif 1'), ('Alternatif 2'), ('Alternatif 3'), ('Alternatif 4'), 
            ('Alternatif 5'), ('Alternatif 6'), ('Alternatif 7'), ('Alternatif 8'), 
            ('Alternatif 9'), ('Alternatif 10'), ('Alternatif 11'), ('Alternatif 12'), 
            ('Alternatif 13'), ('Alternatif 14'), ('Alternatif 15'), ('Alternatif 16'), 
            ('Alternatif 17'), ('Alternatif 18'), ('Alternatif 19'), ('Alternatif 20');
        """

        private const val DATA_TABLE_CRITERIA = """
            INSERT INTO criteria (code, name, weight, type) VALUES
            ("C1", "Harga", 30, "cost"),
            ("C2", "Tahun", 20, "benefit"),
            ("C3", "KM (Jarak Tempuh)", 20, "cost"),
            ("C4", "Bahan Bakar", 20, "cost"),
            ("C5", "Transmisi", 10, "benefit")
        """

        private const val DATA_TABLE_CRITERIA_DETAIL = """
            INSERT INTO criteria_detail (criteria_code, name, level, weight) VALUES
            -- Harga
            ("C1", "250.000 – 1.800.000", "Tinggi", 5),
            ("C1", "1.900.000 – 3.500.000", "Sedang", 3),
            ("C1", "3.600.000 – 5.000.000", "Rendah", 1),
        
            -- Tahun
            ("C2", "2018 – 2020", "Tinggi", 5),
            ("C2", "2015 – 2017", "Sedang", 3),
            ("C2", "2012 – 2014", "Rendah", 1),
        
            -- KM (Jarak Tempuh)
            ("C3", "1000 – 40.000", "Tinggi", 5),
            ("C3", "41.000 – 80.000", "Sedang", 3),
            ("C3", "81.000 – 120.000", "Rendah", 1),
        
            -- Bahan Bakar
            ("C4", "Diesel", "Tinggi", 5),
            ("C4", "Petrol", "Rendah", 3),
        
            -- Transmisi
            ("C5", "Automatic", "Tinggi", 5),
            ("C5", "Manual", "Rendah", 3);
        """

        private const val DATA_TABLE_EVALUATION = """
            INSERT INTO evaluation (alternatif_id, criteria_detail_id) VALUES
            (1, 1), (1, 4), (1, 7), (1, 11), (1, 13),
            (2, 3), (2, 4), (2, 7), (2, 10), (2, 12),
            (3, 1), (3, 5), (3, 7), (3, 11), (3, 13),
            (4, 1), (4, 4), (4, 7), (4, 11), (4, 13),
            (5, 1), (5, 5), (5, 8), (5, 10), (5, 13),
            (6, 1), (6, 4), (6, 7), (6, 11), (6, 13),
            (7, 1), (7, 4), (7, 7), (7, 11), (7, 13),
            (8, 1), (8, 4), (8, 7), (8, 11), (8, 13),
            (9, 1), (9, 4), (9, 7), (9, 10), (9, 13),
            (10, 2), (10, 6), (10, 8), (10, 11), (10, 12),
            (11, 1), (11, 4), (11, 7), (11, 11), (11, 13),
            (12, 1), (12, 4), (12, 7), (12, 10), (12, 12),
            (13, 3), (13, 4), (13, 8), (13, 11), (13, 12),
            (14, 1), (14, 4), (14, 7), (14, 10), (14, 13),
            (15, 1), (15, 4), (15, 9), (15, 10), (15, 13),
            (16, 3), (16, 4), (16, 7), (16, 10), (16, 12),
            (17, 1), (17, 6), (17, 9), (17, 10), (17, 13),
            (18, 1), (18, 4), (18, 7), (18, 10), (18, 13),
            (19, 1), (19, 4), (19, 7), (19, 11), (19, 13),
            (20, 2), (20, 4), (20, 7), (20, 10), (20, 12);
        """

    }
}
