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

        holder.txtTit.setText(note.getTxtTit());
        holder.txtTag.setText(note.getTxtTag());
        holder.txtTxt.setText(note.getTxtTxt());
        holder.txtDat.setText(note.getTxtDat());
    }

    @Override
    public int getItemCount() {
        return listaNotes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtTit;
        TextView txtTag;
        TextView txtTxt;
        TextView txtDat;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTit = itemView.findViewById(R.id.txtTit);
            txtTag = itemView.findViewById(R.id.txtTag);
            txtTxt = itemView.findViewById(R.id.txtTxt);
            txtDat = itemView.findViewById(R.id.txtDat);
        }
    }
}
