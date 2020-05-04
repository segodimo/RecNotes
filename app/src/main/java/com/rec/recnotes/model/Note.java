package com.rec.recnotes.model;

import java.io.Serializable;

public class Note implements Serializable {

    private Long id;

    private String txtTit;
    private String txtTag;
    private String txtTxt;
    private String txtDat;

    // public Note(String txtTit, String txtTag, String txtTxt, String txtDat) {
        // this.txtTit = txtTit;
        // this.txtTag = txtTag;
        // this.txtTxt = txtTxt;
        // this.txtDat = txtDat;
    // }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTxtTit() {
        return txtTit;
    }

    public void setTxtTit(String txtTit) {
        this.txtTit = txtTit;
    }

    public String getTxtTag() {
        return txtTag;
    }

    public void setTxtTag(String txtTag) {
        this.txtTag = txtTag;
    }

    public String getTxtTxt() {
        return txtTxt;
    }

    public void setTxtTxt(String txtTxt) {
        this.txtTxt = txtTxt;
    }

    public String getTxtDat() {
        return txtDat;
    }

    public void setTxtDat(String txtDat) {
        this.txtDat = txtDat;
    }
}
