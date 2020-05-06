package com.rec.recnotes.helper;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.rec.recnotes.model.Note;

import java.util.ArrayList;
import java.util.List;

// DAO Data Access Object = CRUD
public class NoteDAO implements INoteDAO {

    private SQLiteDatabase write;
    private SQLiteDatabase read;

    public NoteDAO(Context context) {
        DBHelper db = new DBHelper(context);
        write = db.getWritableDatabase();
        read = db.getReadableDatabase();

    }

    @Override
    public boolean createNt(Note note) {
        ContentValues cv = new ContentValues();
        cv.put("titulo", note.getTxtTit());
        cv.put("texto", note.getTxtTxt());
        cv.put("tag", note.getTxtTag());
        cv.put("subtag", note.getTxtSubTag());
        cv.put("score", note.getTxtScore());
        cv.put("nivel", note.getTxtNivel());
        cv.put("datatime", note.getTxtDat());
        try{
            write.insert(DBHelper.TABELA_DDS, null, cv);
            Log.i("INFO", "Sucesso createNt dados");
        }catch (Exception e){
            Log.i("INFO", "Paila createNt dados" + e.getMessage());
            return false;
    }
        return true;
    }

    @Override
    public boolean updateNt(Note note) {
        ContentValues cv = new ContentValues();
        cv.put("titulo", note.getTxtTit());
        cv.put("texto", note.getTxtTxt());
        cv.put("tag", note.getTxtTag());
        cv.put("subtag", note.getTxtSubTag());
        cv.put("score", note.getTxtScore());
        cv.put("nivel", note.getTxtNivel());
        cv.put("datatime", note.getTxtDat());
        try{
            String[] args = {note.getId().toString()};
            write.update(DBHelper.TABELA_DDS, cv, "id=?", args);
            Log.i("INFO", "Sucesso updateNt dados");
        }catch (Exception e){
            Log.i("INFO", "Paila updateNt dados" + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteTab() {

        //Cursor cursor = read.rawQuery(sql, null);return false;
        try{
            //String sql = "SELECT * FROM " + DBHelper.TABELA_DDS + " WHERE tag != 'vx' ;";
            write.delete(DBHelper.TABELA_DDS,null,null);
            Log.i("INFO", "Sucesso deleteTab");
        }catch (Exception e){
            Log.i("INFO", "Paila deleteTab" + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteNt(Note note) {
        try{
            String[] args = {note.getId().toString()};
            //write.update(DBHelper.TABELA_DDS, cv, "id=?", args);
            write.delete(DBHelper.TABELA_DDS,"id=?", args );
            Log.i("INFO", "Sucesso updateNt dados");
        }catch (Exception e){
            Log.i("INFO", "Paila updateNt dados" + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public List<Note> listar(boolean io, String flt, String txtflt) {

        List<Note> notes = new ArrayList<>();

        System.out.println("txtflt: "+txtflt+" flt: "+flt);

        //String sql = "SELECT * FROM " + DBHelper.TABELA_DDS + " WHERE tag != 'vx' ;";
        //String sql = "SELECT * FROM " + DBHelper.TABELA_DDS + " WHERE NOT tag LIKE '@@%' ORDER BY tag ASC, texto ASC;";
        //String sql = "SELECT * FROM " + DBHelper.TABELA_DDS + " WHERE NOT tag LIKE '@@%' ORDER BY datatime DESC;";
        String sql = "SELECT * FROM " + DBHelper.TABELA_DDS + " WHERE nivel != 5 ORDER BY datatime DESC;";

        if (io && flt.equals("")){
            //sql = "SELECT * FROM " + DBHelper.TABELA_DDS + " ORDER BY tag ASC, texto ASC;";
            //sql = "SELECT * FROM " + DBHelper.TABELA_DDS + " ORDER BY datatime DESC;";
            sql = "SELECT * FROM " + DBHelper.TABELA_DDS + " WHERE nivel = 5 ORDER BY datatime DESC;";
        }

        else if(io && flt.equals("##")){
            sql = "SELECT * FROM " + DBHelper.TABELA_DDS + " WHERE nivel = 5 ORDER BY titulo ASC ;";
        }
        else if(io && flt.equals("$$")){
            //sql = "SELECT * FROM " + DBHelper.TABELA_DDS + " WHERE tag LIKE + '%@@%' ORDER BY datatime DESC ;";
            sql = "SELECT * FROM " + DBHelper.TABELA_DDS + " WHERE nivel = 5 ORDER BY texto ASC ;";
        }
        else if(io && flt.equals("__")){
            sql = "SELECT * FROM " + DBHelper.TABELA_DDS + " WHERE nivel = 5 ORDER BY tag ASC ;";
        }
        else if(io && flt.equals("&&")){
            sql = "SELECT * FROM " + DBHelper.TABELA_DDS + " WHERE nivel = 5 ORDER BY subtag ASC ;";
        }
        else if(io && flt.equals("--")){
            sql = "SELECT * FROM " + DBHelper.TABELA_DDS + " WHERE nivel = 5 ORDER BY score DESC ;";
            //sql = "SELECT * FROM " + DBHelper.TABELA_DDS + " ORDER BY score DESC ;";
        }

        else if(io && flt.equals("??")){
            //sql = "SELECT * FROM " + DBHelper.TABELA_DDS + " WHERE nivel = 5 AND tag LIKE '%"+txtflt+"%' ORDER BY score ASC ;";
            //sql = "SELECT * FROM " + DBHelper.TABELA_DDS + " WHERE nivel = 5 AND texto LIKE '%"+txtflt+"%' ORDER BY score ASC ;";
            //sql = "SELECT * FROM " + DBHelper.TABELA_DDS + " WHERE texto LIKE '%"+txtflt+"%' ORDER BY texto ASC ;";
            sql = "SELECT * FROM " + DBHelper.TABELA_DDS + " WHERE nivel = 5 AND texto LIKE '%"+txtflt+"%' ORDER BY texto ASC;";
        }

//        else if(io && !flt.equals("")){
//            sql = "SELECT * FROM " + DBHelper.TABELA_DDS + " WHERE tag LIKE + '@@"+flt+"%' ORDER BY tag ASC, texto ASC ;";
//        }

        //ABERTO
        else if(!io && !flt.equals("")){
            //sql = "SELECT * FROM (SELECT * FROM " + DBHelper.TABELA_DDS + " WHERE tag LIKE 'ccc%') WHERE NOT tag LIKE '@@%' ;";
            sql = "SELECT * FROM (SELECT * FROM " + DBHelper.TABELA_DDS + " WHERE NOT tag LIKE '@@%') WHERE tag LIKE '"+flt+"%' ORDER BY tag ASC, texto ASC ;";
        }



        Cursor cursor = read.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {

                Note note = new Note();

                long id = cursor.getLong(0);
                String titulo = cursor.getString(1);
                String texto = cursor.getString(2);
                String tag = cursor.getString(3);
                String subtag = cursor.getString(4);
                long score = cursor.getLong(5);
                long nivel = cursor.getLong(6);
                String datatime = cursor.getString(7);


//                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
//                System.out.println(cursor.getLong(0) + " 0\n"+
//                        cursor.getString(1) + " 1\n"+
//                        cursor.getString(2) + " 2\n"+
//                        cursor.getString(3) + " 3\n"+
//                        cursor.getString(4) + " 4\n"+
//                        cursor.getString(5) + " 5\n"+
//                        cursor.getString(6) + " 6");
//                System.out.println("+++++++++++++++++++++++++++++++++++++++++");

                note.setId(id);
                note.setTxtTit(titulo);
                note.setTxtTxt(texto);
                note.setTxtTag(tag);
                note.setTxtSubTag(subtag);
                note.setTxtScore(score);
                note.setTxtNivel(nivel);
                note.setTxtDat(datatime);

                notes.add(note);

            } while (cursor.moveToNext());
        }

        return notes;
    }
}
