package com.example.rahmatridham.mynato.Model;

/**
 * Created by rahmatridham on 3/3/2017.
 */

public class AbsensiModel {
    String nama, nipeg;
    boolean selected;

    public AbsensiModel(String nama, String nipeg,boolean selected) {
        this.nama = nama;
        this.nipeg = nipeg;
        this.selected = selected;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNipeg() {
        return nipeg;
    }

    public void setNipeg(String nipeg) {
        this.nipeg = nipeg;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
