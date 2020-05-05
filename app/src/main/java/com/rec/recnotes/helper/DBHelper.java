package com.rec.recnotes.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static int VERSION = 1;
    public static String NOME_DB = "REC_NOTES_01";
    public static String TABELA_DDS = "dds_tabela";

    public DBHelper(@Nullable Context context) {
        super(context, NOME_DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE IF NOT EXISTS "+TABELA_DDS+" ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "titulo TEXT NOT NULL, " +
                "texto TEXT NOT NULL, " +
                "tag TEXT, " +
                "subtag TEXT, " +
                "score INTEGER, " +
                "nivel INTEGER, " +
                "datatime DATETIME DEFAULT CURRENT_TIMESTAMP ) ";

        try{
            db.execSQL( sql );
            Log.i("INFO DB", "Succeso ao criar a tabela");
        }catch(Exception e){
            /*Toast.makeText(getApplicationContext(), "PROBLEMAS (-_-)", Toast.LENGTH_SHORT).show();
            e.printStackTrace();*/
            Log.i("INFO DB", "Error criando tabela" + e.getMessage());
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABELA_DDS + " ;" ;

        try{
            db.execSQL( sql );
            onCreate(db);
            Log.i("INFO DB", "Succeso ao atualizar a tabela");
        }catch(Exception e){
            /*Toast.makeText(getApplicationContext(), "PROBLEMAS (-_-)", Toast.LENGTH_SHORT).show();
            e.printStackTrace();*/
            Log.i("INFO DB", "Error atualizar tabela" + e.getMessage());
        }
    }
}
