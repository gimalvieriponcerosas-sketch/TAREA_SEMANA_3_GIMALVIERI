package com.example.tarea_semana_3_gim;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "programa_escolar.db";
    private static final int DB_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE eventos (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "codigo INTEGER UNIQUE, " +
                        "dia TEXT NOT NULL, " +
                        "hora TEXT NOT NULL, " +
                        "actividad TEXT NOT NULL)"
        );

        db.execSQL(
                "CREATE TABLE fragmentos (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "codigo INTEGER UNIQUE, " +
                        "titulo TEXT NOT NULL, " +
                        "autor TEXT NOT NULL, " +
                        "texto TEXT NOT NULL)"
        );

        insertarDatosIniciales(db);
    }

    private void insertarDatosIniciales(SQLiteDatabase db) {
        db.execSQL("INSERT INTO eventos (codigo, dia, hora, actividad) VALUES (1, 'LUNES', '9:00 am', 'Inauguración')");
        db.execSQL("INSERT INTO eventos (codigo, dia, hora, actividad) VALUES (2, 'LUNES', '11:00 am', 'Danzas típicas')");
        db.execSQL("INSERT INTO eventos (codigo, dia, hora, actividad) VALUES (3, 'MARTES', '9:00 am', 'Concurso de dibujo')");
        db.execSQL("INSERT INTO eventos (codigo, dia, hora, actividad) VALUES (4, 'MARTES', '11:00 am', 'Feria de ciencias')");
        db.execSQL("INSERT INTO eventos (codigo, dia, hora, actividad) VALUES (5, 'MIÉRCOLES', '9:00 am', 'Lectura de poesía')");
        db.execSQL("INSERT INTO eventos (codigo, dia, hora, actividad) VALUES (6, 'MIÉRCOLES', '11:00 am', 'Teatro escolar')");
        db.execSQL("INSERT INTO eventos (codigo, dia, hora, actividad) VALUES (7, 'JUEVES', '9:00 am', 'Exposición de proyectos')");
        db.execSQL("INSERT INTO eventos (codigo, dia, hora, actividad) VALUES (8, 'JUEVES', '11:00 am', 'Música folclórica')");
        db.execSQL("INSERT INTO eventos (codigo, dia, hora, actividad) VALUES (9, 'VIERNES', '9:00 am', 'Clausura y premiación')");

        db.execSQL("INSERT INTO fragmentos (codigo, titulo, autor, texto) VALUES (" +
                "1, 'El Principito', " +
                "'Antoine de Saint-Exupéry', " +
                "'Cuando yo tenía seis años vi en un libro sobre la selva virgen, que se titulaba Historias vividas, una magnífica lámina. Representaba una serpiente boa que se tragaba a una fiera.')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS eventos");
        db.execSQL("DROP TABLE IF EXISTS fragmentos");
        onCreate(db);
    }

    // ---------- EVENTOS (CRUD por codigo) ----------
    public Cursor obtenerEventos() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM eventos ORDER BY id", null);
    }

    public long insertarEvento(int codigo, String dia, String hora, String actividad) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("codigo", codigo);
        values.put("dia", dia);
        values.put("hora", hora);
        values.put("actividad", actividad);
        return db.insert("eventos", null, values);
    }

    public int actualizarEvento(int codigo, String dia, String hora, String actividad) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("dia", dia);
        values.put("hora", hora);
        values.put("actividad", actividad);
        return db.update("eventos", values, "codigo=?", new String[]{String.valueOf(codigo)});
    }

    public int eliminarEvento(int codigo) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete("eventos", "codigo=?", new String[]{String.valueOf(codigo)});
    }

    // ---------- FRAGMENTOS (CRUD por codigo) ----------
    public Cursor obtenerFragmentos() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM fragmentos ORDER BY id", null);
    }

    public long insertarFragmento(int codigo, String titulo, String autor, String texto) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("codigo", codigo);
        values.put("titulo", titulo);
        values.put("autor", autor);
        values.put("texto", texto);
        return db.insert("fragmentos", null, values);
    }

    public int actualizarFragmento(int codigo, String titulo, String autor, String texto) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", titulo);
        values.put("autor", autor);
        values.put("texto", texto);
        return db.update("fragmentos", values, "codigo=?", new String[]{String.valueOf(codigo)});
    }

    public int eliminarFragmento(int codigo) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete("fragmentos", "codigo=?", new String[]{String.valueOf(codigo)});
    }
}