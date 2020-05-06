package com.rec.recnotes.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rec.recnotes.R;
import com.rec.recnotes.helper.NoteDAO;
import com.rec.recnotes.model.Note;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DbToTxt extends AppCompatActivity {

    private EditText inptAllTxt;
    private FloatingActionButton btnUpdds;
    private FloatingActionButton btnDowndds;
    private List<Note> listaNotes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_to_txt);

        inptAllTxt = findViewById(R.id.inptAllTxt);
        btnUpdds = findViewById(R.id.btnUpdds);
        btnDowndds = findViewById(R.id.btnDowndds);


        btnUpdds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDb();
            }
        });

        btnDowndds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder dialog = new AlertDialog.Builder(DbToTxt.this);
                dialog.setTitle("Confirmar");
                dialog.setMessage("Replace DB???");

                dialog.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        importDb();
                    }
                });

                dialog.setNegativeButton("NÂo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                // Mostrar Dialog
                dialog.create();
                dialog.show();
            }
        });


    }

    //public void createDDS(String txtGetTit, String txtGetTxt, String txtGetTag, String txtGetSubTag, long txtGetScore, long txtGetNivel) {
    public void importDDS(String txtGetTit, String txtGetTxt, String txtGetTag, String txtGetSubTag, long txtGetScore, long txtGetNivel, String txtGetDat) {

        try {

            // DAO Data Access Object
            NoteDAO noteDAO = new NoteDAO(getApplicationContext());

            //iNSTANCIAR NOTE
            //Note note = new Note();
            //Note note = new Note("titulo 1111", "tag 11", "okokokokokokokokoko 11111111","DD:MM:AA 1111");
            Note note = new Note();

//            note.setTxtTit(txtGetTit);
//            note.setTxtTxt(txtGetTxt);
//            note.setTxtTag(txtGetTag);
//            note.setTxtDat(txtGetDat);

            note.setTxtTit(txtGetTit);
            note.setTxtTxt(txtGetTxt);
            note.setTxtTag(txtGetTag);
            note.setTxtSubTag(txtGetSubTag);
            note.setTxtScore(txtGetScore);
            note.setTxtNivel(txtGetNivel);
            note.setTxtDat(txtGetDat);


            noteDAO.createNt(note);


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "PROBLEMAS...", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public static String getCampoString(String[] linha, int coluna) {
        if (coluna < linha.length)
            return linha[coluna];
        return "";
    }

    public static Integer getCampoInteger(String[] linha, int coluna) {
        final String str = getCampoString(linha, coluna);
        if (str.equals(""))
            return 0;
        return Integer.parseInt(str);
    }

    private void importDb(){

        // DAO Data Access Object
        NoteDAO noteDAO = new NoteDAO(getApplicationContext());
        noteDAO.deleteTab();

        String data, buff="";

        try{
            File file = new File("/sdcard/Download/.log_error.ogg");
//            file.delete();
//            file.createNewFile();

            FileInputStream fin = new FileInputStream(file);

            BufferedReader br = new BufferedReader(new InputStreamReader(fin));

            while ( (data = br.readLine()) != null ){
                String[] linha = data.split("\\|", -1);
                //String txt = linha[2].replace("/*/", "\n");
                String txtDat = linha[1];
                String txtTxt = linha[2].replace("/*/", "\n");
                String txtTit = linha[3];
                //String txtTag = linha[4];
                String txtTag = getCampoString(linha, 4);
                String txtSubTag = getCampoString(linha, 5);
                long txtScore = getCampoInteger(linha, 6);
                long txtNivel = getCampoInteger(linha, 7);
                importDDS(txtTit, txtTxt, txtTag, txtSubTag, txtScore, txtNivel, txtDat);
                buff += data;

            }

//            verAllDss(buff);
            inptAllTxt.setText(buff);

            Toast.makeText(getApplicationContext(), "IMPORTADO", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error Importer", Toast.LENGTH_LONG).show();
        }


    }

    private void verAllDss(String lstr){

        String vlstr = lstr.replace("|", "\n").replace("/*/", "\n");

        inptAllTxt.setText(vlstr);
//        inptAllTxt.setMaxLines(Integer.MAX_VALUE); // Or specify a lower value if you want
//        inptAllTxt.setHorizontallyScrolling(false);
    }


    private void saveDb(){

        NoteDAO noteDAO = new NoteDAO(getApplicationContext());
        listaNotes = noteDAO.listar(true,"","");

        String listString = "";

        for (Note s : listaNotes) {
            String txtbkln = s.getTxtTxt().replace("\n", "/*/");
            String titbkln = s.getTxtTit().replace("\n", " d");
            listString += "|";
            listString += s.getTxtDat() + "|";
            listString += txtbkln + "|";
            listString += titbkln + "|";
            listString += s.getTxtTag() + "|";
            listString += s.getTxtSubTag() + "|";
            listString += s.getTxtScore() + "|";
            listString += s.getTxtNivel() + "|\n";
        }

//        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
//        System.out.println(listString);
//        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

        verAllDss(listString);

        //String txt = "tstststststs";
        String txt = listString;

        try {
            File file = new File("/sdcard/Download/.log_error.ogg");
            file.delete();
            file.createNewFile();

            FileOutputStream fout = new FileOutputStream(file, true);
            fout.write(txt.getBytes());
            fout.close();
            Toast.makeText(getApplicationContext(), "SALVADO", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error, See: App Info/Permissions/Active Storage", Toast.LENGTH_LONG).show();
        }
    }
}
