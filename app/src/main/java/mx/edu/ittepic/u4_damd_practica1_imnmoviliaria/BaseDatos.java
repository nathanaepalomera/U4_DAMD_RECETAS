package mx.edu.ittepic.u4_damd_practica1_imnmoviliaria;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDatos extends SQLiteOpenHelper {
    public BaseDatos(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //SE EJECUTA CUANDO LA APLICACION EJERCICIO1 SE INICIA
        //SIRVE PARA CONSTRUIR EN SQLLITE EN EL CELULAR LAS TABLAS QUE LA APP REQUIERE PARA FUNCIONAR HACE CUALQUIER INSTRUCCION SQL
        db.execSQL("CREATE TABLE RECETAS (ID INTEGER PRIMARY KEY NOT NULL,NOMBRE VARCHAR(200),INGREDIENTES VARCHAR(1000),PREPARACION VARCHAR(1000),OBSERVACIONES VARCHAR (1000))");//funciona para insert, delte, udpate, create table
        //db.rawQuery();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
