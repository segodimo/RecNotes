package com.rec.recnotes.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rec.recnotes.R;
import com.rec.recnotes.helper.NoteDAO;
import com.rec.recnotes.model.Note;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class EditionActivity extends AppCompatActivity {

    private TextView txtId;
    private EditText txtTit;
    private EditText txtTxt;
    private EditText txtTag;
    private EditText txtSubTag;
    private EditText txtScore;
    private EditText txtNivel;
    private TextView txtDat;
    private FloatingActionButton btnSalva;
    private Note noteAtual;
    private static final String ARQUIVO_PREFERENCIAS = "ArquivoPreferencias";
    private ConstraintLayout cnstrntLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edition);

        txtId = findViewById(R.id.txtId);
        txtTit = findViewById(R.id.txtTit);
        txtTxt = findViewById(R.id.txtTxt);
        txtTag = findViewById(R.id.txtTag);
        txtSubTag = findViewById(R.id.txtSubTag);
        txtScore = findViewById(R.id.txtScore);
        txtNivel = findViewById(R.id.txtNivel);
        txtDat = findViewById(R.id.txtDat);
        btnSalva = findViewById(R.id.btnSalva);
        cnstrntLayout = findViewById(R.id.cnstrntLayout);


//        Random rnd = new Random();
//        int color = Color.argb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
//        findViewById(android.R.id.content).setBackgroundColor(color);

        //Recuperar Dados Enviados
        Bundle dds = getIntent().getExtras();

        Boolean ddsisNote = dds.getBoolean("isnote");

        if (ddsisNote) {
            int ddsNoteId = dds.getInt("noteId");
            //txtId.setText(String.valueOf(ddsNoteId+1));

            //Recuperando Note
            noteAtual = (Note) getIntent().getSerializableExtra("selectNote");

            txtId.setText(noteAtual.getId().toString());
            txtTit.setText(noteAtual.getTxtTit());
            txtTxt.setText(noteAtual.getTxtTxt());
            txtTag.setText(noteAtual.getTxtTag());
            txtSubTag.setText(noteAtual.getTxtSubTag());
            txtScore.setText(noteAtual.getTxtScore().toString());
            txtNivel.setText(noteAtual.getTxtNivel().toString());
            txtDat.setText(noteAtual.getTxtDat());

            System.out.println(">>>>"+noteAtual.getId().toString());

        } else {
            // Paste ClipBoard
            //Toast.makeText(getApplicationContext(), "Paste ClipBoard", Toast.LENGTH_SHORT).show();
            pasteClipBoard();
            txtId.setVisibility(View.INVISIBLE);
            txtDat.setVisibility(View.INVISIBLE);
        }

        cnstrntLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(EditionActivity.this);
            }
        });

        // Se clicar no boton
        btnSalva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long txtGetScore = 0;
                long txtGetNivel = 0;

                NoteDAO noteDAO = new NoteDAO(getApplicationContext());

                String txtGetTit = txtTit.getText().toString();
                String txtGetTxt = txtTxt.getText().toString();
                String txtGetTag = txtTag.getText().toString();
                String txtGetSubTag = txtSubTag.getText().toString();
//                txtGetScore = Integer.parseInt(txtScore.getText().toString());
//                txtGetNivel = Integer.parseInt(txtNivel.getText().toString());

                Bundle dds = getIntent().getExtras();
                Boolean ddsisNote = dds.getBoolean("isnote");
                Boolean ddsAutoNote = dds.getBoolean("autoNote");

                //Toast.makeText(getApplicationContext(), txtGetTag+" !!", Toast.LENGTH_SHORT).show();

                if (txtGetTag.equals("@@@") && txtGetTxt.equals("")) {
                    ioSwitchFiltro(true,"",false, false);
                } else if (txtGetTag.equals("@@@") && !txtGetTxt.equals("")) {
                    ioSwitchFiltro(true,txtGetTxt,false, false);
                } else if (txtGetTag.equals("@@@") && !txtGetTxt.equals("##")) {
                    ioSwitchFiltro(true,"##",false, false);
                } else if (txtGetTag.equals("@@@@") && txtGetTag.equals("&&")) {
                    ioSwitchFiltro(true,"&&",false, false);
                } else if (txtGetTag.equals("@@@@") && txtGetTag.equals("$$")) {
                    ioSwitchFiltro(true,"$$",false, false);
                } else if (txtGetTxt.equals("??")) {
                    fltFiltro(txtGetTag);
                } else if (txtGetTag.equals("@@++")) {
                    ioSwitchFiltro(true,"",true, false);
                } else if (txtGetTag.equals("@@##")) {
                    ioSwitchFiltro(true,"",false, true);
                } else if (txtGetTag.equals("+++")) {
                    ioSwitchFiltro(false,"",true, false);
                } else if (txtGetTag.equals("#+++")) {
                    ioSwitchFiltro(false,"",false, true);
                } else if (txtGetTag.equals("//")) {
                    ioSwitchFiltro(false,"",false, false);
                } else if (txtGetTag.equals("£===")) {
                    deletTab();
                } else if (txtGetTag.equals(",,,")) {
                    dbToTxt();
                } else {
                    if (ddsisNote) {
                        //Fazer Update de Dados
                        //
                        // btnSalva.setVisibility(View.INVISIBLE);
                        updateDDS(txtGetTit, txtGetTxt, txtGetTag, txtGetSubTag, txtGetScore, txtGetNivel);

                        finish();
                    } else {
                        // Paste ClipBoard
                        createDDS(txtGetTit, txtGetTxt, txtGetTag, txtGetSubTag, txtGetScore, txtGetNivel);
                        //Toast.makeText(EditionActivity.this, txtGetSubTag+"okokok", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });



    }

    public void deletTab(){
        NoteDAO noteDAO = new NoteDAO(getApplicationContext());
        noteDAO.deleteTab();
        Toast.makeText(this, "Tab Deletada", Toast.LENGTH_SHORT).show();
        finish();
    }

    public  void dbToTxt(){
        Intent intent = new Intent(getApplicationContext(), DbToTxt.class);
        startActivity(intent);
        finish();
    }

    public void fltFiltro(String flt) {
        SharedPreferences preferences = getSharedPreferences(ARQUIVO_PREFERENCIAS, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("fltfiltro", flt);
        editor.commit();
        finish();
    }

    public void ioSwitchFiltro(boolean io, String flt, boolean ioauto, boolean iocpoy) {
        SharedPreferences preferences = getSharedPreferences(ARQUIVO_PREFERENCIAS, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("ioverfiltro", io);
        editor.putString("fltfiltro", flt);
        editor.putBoolean("ouvinteIO", ioauto);
        editor.putBoolean("autoCopyIO", iocpoy);
        editor.commit();
        if (io)
            Toast.makeText(getApplicationContext(), "(-_<)", Toast.LENGTH_SHORT).show();
        if (!io)
            Toast.makeText(getApplicationContext(), "(-_-)", Toast.LENGTH_SHORT).show();

        finish();
    }

    public void imprimeTxt(String verTxt) {

        boolean ioVer = false;
        SharedPreferences preferences = getSharedPreferences(ARQUIVO_PREFERENCIAS, 0);
        if (preferences.contains("ioverfiltro")) {
            ioVer = preferences.getBoolean("ioverfiltro", false);
            if(ioVer)
                txtTag.setText("@@");
                txtNivel.setText("5");
        }


        int lmtTit = 30;
        if (verTxt.length() > lmtTit) {
            txtTit.setText(verTxt.substring(0, lmtTit) + "...");
            txtTxt.setText(verTxt);
            //createDDS(txtGetTit, txtGetTxt, txtGetTag);
        } else {
            txtTit.setText(verTxt);
            txtTxt.setText(verTxt);
            //createDDS(txtGetTit, txtGetTxt, txtGetTag);
        }
    }

    private void pasteClipBoard() {
        ClipboardManager mclipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        boolean isTextPlain = mclipboard.getPrimaryClip().getDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);
        boolean isTextHtml = mclipboard.getPrimaryClip().getDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML);
        boolean isTextUrilist = mclipboard.getPrimaryClip().getDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_URILIST);
        boolean isTextIntent = mclipboard.getPrimaryClip().getDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_INTENT);

        CharSequence text = null;

        if (isTextPlain) {
            ClipData clipData = mclipboard.getPrimaryClip();
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("1 - MIMETYPE_TEXT_PLAIN");
            System.out.println(clipData.toString());
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

            ClipData.Item item = clipData.getItemAt(0);
            if (item != null) {
                text = item.getText();
                if (text == null) {
                    // taken from source of clipData.getText() method
                    imprimeTxt("ERROR o texto = MIMETYPE_TEXT_PLAIN com problema");
                } else {
                    imprimeTxt(text.toString());
                }
            }
        } else if (isTextHtml) {
            ClipData clipData = mclipboard.getPrimaryClip();
            ClipData.Item item = clipData.getItemAt(0);
            imprimeTxt(item.getText().toString());
        } else if (isTextUrilist) {
            ClipData clipData = mclipboard.getPrimaryClip();
            imprimeTxt("ERROR o texto = MIMETYPE_TEXT_URILIST");
        } else if (isTextIntent) {
            ClipData clipData = mclipboard.getPrimaryClip();
            imprimeTxt("ERROR o texto = MIMETYPE_TEXT_INTENT");
        } else {
            ClipData clipData = mclipboard.getPrimaryClip();
            imprimeTxt("ERROR o texto = NULL");
        }
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    //CRUD
    public void createDDS(String txtGetTit, String txtGetTxt, String txtGetTag, String txtGetSubTag, long txtGetScore, long txtGetNivel) {

        try {

            // DAO Data Access Object
            NoteDAO noteDAO = new NoteDAO(getApplicationContext());

            Note note = new Note();

            note.setTxtTit(txtGetTit);
            note.setTxtTxt(txtGetTxt);
            note.setTxtTag(txtGetTag);
            note.setTxtSubTag(txtGetSubTag);
            note.setTxtScore(txtGetScore);
            note.setTxtNivel(txtGetNivel);
            note.setTxtDat(getDateTime());

            noteDAO.createNt(note);

            boolean ouvinteIO = false;
            SharedPreferences preferences = getSharedPreferences(ARQUIVO_PREFERENCIAS, 0);
            if (preferences.contains("ouvinteIO")) {
                ouvinteIO = preferences.getBoolean("ouvinteIO", false);
                if(!ouvinteIO) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    assert clipboard != null;
                    clipboard.setText("");
                }
            }
            finish();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "PROBLEMAS...", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    //Atualizar no DB
    public void updateDDS(String txtGetTit, String txtGetTxt, String txtGetTag, String txtGetSubTag, long txtGetScore, long txtGetNivel) {
        try {
            // DAO Data Access Object
            NoteDAO noteDAO = new NoteDAO(getApplicationContext());
            Note note = new Note();
            note.setTxtTit(txtGetTit);
            note.setTxtTxt(txtGetTxt);
            note.setTxtTag(txtGetTag);
            note.setTxtSubTag(txtGetSubTag);
            note.setTxtScore(txtGetScore);
            note.setTxtNivel(txtGetNivel);
            note.setTxtDat(getDateTime());
            note.setId(noteAtual.getId());
            //noteDAO.createNt(note);
            noteDAO.updateNt(note);
            finish();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "PROBLEMAS...", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
