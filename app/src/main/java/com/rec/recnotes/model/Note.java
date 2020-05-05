package com.rec.recnotes.model;

import java.io.Serializable;

public class Note implements Serializable {

    private Long id;

    private String txtTit;
    private String txtTxt;
    private String txtTag;
    private String txtSubTag;
    private Long txtScore;
    private Long txtNivel;
    private String txtDat;

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

    public String getTxtSubTag() {
        return txtSubTag;
    }

    public void setTxtSubTag(String txtSubTag) {
        this.txtSubTag = txtSubTag;
    }

    public Long getTxtScore() {
        return txtScore;
    }

    public void setTxtScore(Long txtScore) {
        this.txtScore = txtScore;
    }

    public Long getTxtNivel() {
        return txtNivel;
    }

    public void setTxtNivel(Long txtNivel) {
        this.txtNivel = txtNivel;
    }
}
