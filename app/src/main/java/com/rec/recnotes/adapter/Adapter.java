package com.rec.recnotes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rec.recnotes.R;
import com.rec.recnotes.model.Note;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private List<Note> listaNotes;

    public Adapter(List<Note> lista) {
        this.listaNotes = lista;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_lista, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


//        holder.txtTit.setText("tititititit");
//        holder.txtTag.setText("tagtagtag");
//        holder.txtTxt.setText("okokokokokokokokokookokok");
//        holder.txtDat.setText("0000-00-00 00:00:00");

        Note note = listaNotes.get( position );

        holder.txtId.setText(note.getId().toString());
        holder.txtTit.setText(note.getTxtTit());
        holder.txtTxt.setText(note.getTxtTxt());
        holder.txtTag.setText(note.getTxtTag());
        holder.txtSubTag.setText(note.getTxtSubTag());
        holder.txtScore.setText(note.getTxtScore().toString());
        holder.txtNivel.setText(note.getTxtNivel().toString());
        holder.txtDat.setText(note.getTxtDat());
    }

    @Override
    public int getItemCount() {
        return listaNotes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtId;
        TextView txtTit;
        TextView txtTxt;
        TextView txtTag;
        TextView txtSubTag;
        TextView txtScore;
        TextView txtNivel;
        TextView txtDat;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtId = itemView.findViewById(R.id.txtId);
            txtTit = itemView.findViewById(R.id.txtTit);
            txtTxt = itemView.findViewById(R.id.txtTxt);
            txtTag = itemView.findViewById(R.id.txtTag);
            txtSubTag = itemView.findViewById(R.id.txtSubTag);
            txtScore = itemView.findViewById(R.id.txtScore);
            txtNivel = itemView.findViewById(R.id.txtNivel);
            txtDat = itemView.findViewById(R.id.txtDat);
        }
    }
}
