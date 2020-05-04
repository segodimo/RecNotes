package com.rec.recnotes.helper;

import com.rec.recnotes.model.Note;

import java.util.List;

public interface INoteDAO {
    public boolean createNt(Note note);
    //public boolean readNt(Note note);
    public boolean updateNt(Note note);
    public boolean deleteNt(Note note);
    public boolean deleteTab();
    public List<Note> listar(boolean io, String flt);
}
