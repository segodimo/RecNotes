package com.rec.recnotes.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rec.recnotes.R;
import com.rec.recnotes.helper.NoteDAO;
import com.rec.recnotes.helper.RecyclerItemClickListener;
import com.rec.recnotes.adapter.Adapter;
import com.rec.recnotes.model.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private Adapter adapter;
    private List<Note> listaNotes = new ArrayList<>();
    private Note note;
    private FloatingActionButton btnPlus;
    private static final String ARQUIVO_PREFERENCIAS = "ArquivoPreferencias";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new Adapter(listaNotes);
        btnPlus = findViewById(R.id.btnPlus);
        ioSwitchFiltro(false, false, false);
        //Toast.makeText(this, "...", Toast.LENGTH_SHORT).show();
        ouvinteClipBoard();
        swip();


        // Adicionar evento de click
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {

                            @Override
                            public void onItemClick(View view, int position) {
                                //Toast.makeText(getApplicationContext(), "Item Precionado", Toast.LENGTH_SHORT).show();
                                enviarDDS(true, position);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                //Toast.makeText(getApplicationContext(), "Item Precionado Longo", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //Toast.makeText(getApplicationContext(), "MotionEvent", Toast.LENGTH_SHORT).show();
                            }

                        }
                )

        );

        // btnPlus
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "OKOKOKOKOKOK", Toast.LENGTH_SHORT).show();
                enviarDDS(false, -1);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Toast.makeText(getApplicationContext(), "onRefresh", Toast.LENGTH_SHORT).show();
                ioSwitchFiltro(false, false, false);
                cargarListaNotes();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

//

    }


    private void  ouvinteClipBoard(){
        
        final ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.addPrimaryClipChangedListener( new ClipboardManager.OnPrimaryClipChangedListener() {
            public void onPrimaryClipChanged() {
                String a = clipboard.getText().toString();
                //Toast.makeText(getBaseContext(),"Copy:\n"+a,Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), "copiado", Toast.LENGTH_SHORT).show();
                boolean ouvinteIO = false;
                SharedPreferences preferences = getSharedPreferences(ARQUIVO_PREFERENCIAS, 0);
                if (preferences.contains("ouvinteIO")) {
                    ouvinteIO = preferences.getBoolean("ouvinteIO", false);
                    if(ouvinteIO) {
                        enviarDDS(false, -1);
                    }
                }
                boolean autoCopyIO = false;
                if (preferences.contains("autoCopyIO")) {
                    autoCopyIO = preferences.getBoolean("autoCopyIO", false);
                    if(autoCopyIO) {
                        imprimeTxtParaAutoSave(a);
                    }
                }
                
            }
        });
        
    }


    public void imprimeTxtParaAutoSave(String verTxt) {

        String sTit = "";
        String sTxt = "";
        String sTag = "";
        long sNiv = 0;

        boolean ioVer = false;
        SharedPreferences preferences = getSharedPreferences(ARQUIVO_PREFERENCIAS, 0);
        if (preferences.contains("ioverfiltro")) {
            ioVer = preferences.getBoolean("ioverfiltro", false);
            if(ioVer){
                sTag="@@";
                sNiv = 5;
            }
        }


        int lmtTit = 30;
        if (verTxt.length() > lmtTit) {
            sTit = verTxt.substring(0, lmtTit) + "...";
            sTxt = verTxt;
            createDDS(sTit, sTxt, sTag, sNiv);
        } else {
            sTit = verTxt;
            sTxt = verTxt;
            createDDS(sTit, sTxt, sTag, sNiv);
        }
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void createDDS(String txtGetTit, String txtGetTxt, String txtGetTag, long txtGetNivel) {

       try {
           // DAO Data Access Object
           NoteDAO noteDAO = new NoteDAO(getApplicationContext());
           Note note = new Note();
           note.setTxtTit(txtGetTit);
           note.setTxtTxt(txtGetTxt);
           note.setTxtTag(txtGetTag);
           note.setTxtNivel(txtGetNivel);
           note.setTxtDat(getDateTime());
           noteDAO.createNt(note);

           Toast.makeText(this, "Note Salvo", Toast.LENGTH_SHORT).show();

           cargarListaNotes();

       } catch (Exception e) {
           Toast.makeText(getApplicationContext(), "PROBLEMAS...", Toast.LENGTH_SHORT).show();
           e.printStackTrace();
       }
    }


    public void swip() {
        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipFlags = ItemTouchHelper.START | ItemTouchHelper.END | ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                return makeMovementFlags(dragFlags, swipFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                switch (direction) {
                    case ItemTouchHelper.START:
                        removeNote(viewHolder.getAdapterPosition());
                        cargarListaNotes();
                        break;
                    case ItemTouchHelper.END:
                        setClipBoard(viewHolder.getAdapterPosition());
                        cargarListaNotes();
                        break;
                    case ItemTouchHelper.DOWN:
                        abrirLink(viewHolder.getAdapterPosition());
                        cargarListaNotes();
                        break;
                    case ItemTouchHelper.UP:
                        //Toast.makeText(getApplicationContext(), "up", Toast.LENGTH_SHORT).show();
                        //ioSwitchFiltro(false);
                        //cargarListaNotes();
                        abrirLink(viewHolder.getAdapterPosition());
                        cargarListaNotes();
                        break;
                }

            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView);

    }

    public void muteAudio(){
        //mute audio
        AudioManager amanager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
        amanager.setStreamMute(AudioManager.STREAM_ALARM, true);
        amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);
        amanager.setStreamMute(AudioManager.STREAM_RING, true);
        amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
    }


    public void removeNote(int position) {

        note = listaNotes.get(position); //Recupera toda la Note

        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("Confirmar");
        dialog.setMessage("Excluir Notes");

        dialog.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                NoteDAO noteDAO = new NoteDAO(getApplicationContext());

                if (noteDAO.deleteNt(note)) {
                    Toast.makeText(getApplicationContext(), "Datos Apagados " + note.getId(), Toast.LENGTH_SHORT).show();
                    cargarListaNotes();
                }

                cargarListaNotes();
            }
        });

        dialog.setNegativeButton("NÂo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                        //ioSwitchFiltro(false, false, false);
                        cargarListaNotes();
            }
        });
        // Mostrar Dialog
        dialog.create();
        dialog.show();

        //Toast.makeText(getApplicationContext(), "removeNote "+note.getId(), Toast.LENGTH_SHORT).show();
    }

    public void setClipBoard(int position) {
        note = listaNotes.get(position);

        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        assert clipboard != null;
        clipboard.setText(note.getTxtTxt());
        //clipboard.getText();

        //Toast.makeText(getApplicationContext(), "copiado", Toast.LENGTH_SHORT).show();
    }


    public void abrirLink(int position) {
        note = listaNotes.get(position); //Recupera toda la Note
        //String url = "https://www.udemy.com/";
        String url = note.getTxtTxt();
        boolean eslink = URLUtil.isValidUrl(url);
        if(eslink){
            muteAudio();
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
    }

    public void enviarDDS(Boolean isnote, int id) {


        Intent intent = new Intent(getApplicationContext(), EditionActivity.class);

        if (isnote) {
            // Recuperando Notausando Note
            Note selectNote = listaNotes.get(id);
            // PASSAR DADOS
            intent.putExtra("isnote", true);
            intent.putExtra("selectNote", selectNote);
            intent.putExtra("autoNote", false);
        } else if(id==-2){
            intent.putExtra("isnote", false);
            intent.putExtra("autoNote", true);
        } else{
            intent.putExtra("isnote", false);
            intent.putExtra("autoNote", false);
        }
        startActivity(intent);
    }

    public void cargarListaNotes() {

        boolean ioVer = false;
        String flt = "";
        String txtflt = "";
        SharedPreferences preferences = getSharedPreferences(ARQUIVO_PREFERENCIAS, 0);
        if (preferences.contains("ioverfiltro")) {
            ioVer = preferences.getBoolean("ioverfiltro", false);
            flt = preferences.getString("fltfiltro", "");
            txtflt = preferences.getString("txtfltfiltro", "");
            //Toast.makeText(getApplicationContext(), "txtflt "+txtflt, Toast.LENGTH_SHORT).show();
        }


        NoteDAO noteDAO = new NoteDAO(getApplicationContext());
        //listaNotes = noteDAO.listar(ioVer, "");
        listaNotes = noteDAO.listar(ioVer, flt, txtflt);

        // Configurar Adapter
        Adapter adapter = new Adapter(listaNotes);

        // Configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        recyclerView.setAdapter(adapter);

    }

    public void ioSwitchFiltro(boolean io, boolean ioauto, boolean iocpoy) {
        SharedPreferences preferences = getSharedPreferences(ARQUIVO_PREFERENCIAS, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("ioverfiltro", io);
        editor.putString("fltfiltro", "");
        editor.putBoolean("ouvinteIO", ioauto);
        editor.putBoolean("autoCopyIO", iocpoy);
        editor.commit();
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        assert clipboard != null;
        clipboard.setText("");
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Toast.makeText(getApplicationContext(), "onStart...", Toast.LENGTH_SHORT).show();
        cargarListaNotes();
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Toast.makeText(getApplicationContext(), "onDestroy...", Toast.LENGTH_SHORT).show();
//        ioSwitchFiltro(false);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Toast.makeText(getApplicationContext(), "onStop...", Toast.LENGTH_SHORT).show();
//        ioSwitchFiltro(false);
//    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        Toast.makeText(getApplicationContext(), "onPause...", Toast.LENGTH_SHORT).show();
//    }
}
