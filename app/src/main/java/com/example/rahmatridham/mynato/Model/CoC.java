package com.example.rahmatridham.mynato.Model;

/**
 * Created by rahmatridham on 2/21/2017.
 */

public class CoC {
    String id_coc_activity, id_group_coc, keterangan_coc, date;

    public CoC(String id_coc_activity, String id_group_coc, String keterangan_coc, String date) {
        this.id_coc_activity = id_coc_activity;
        this.id_group_coc = id_group_coc;
        this.keterangan_coc = keterangan_coc;
        this.date = date;
    }

    public String getId_coc_activity() {
        return id_coc_activity;
    }

    public void setId_coc_activity(String id_coc_activity) {
        this.id_coc_activity = id_coc_activity;
    }

    public String getId_group_coc() {
        return id_group_coc;
    }

    public void setId_group_coc(String id_group_coc) {
        this.id_group_coc = id_group_coc;
    }

    public String getKeterangan_coc() {
        return keterangan_coc;
    }

    public void setKeterangan_coc(String keterangan_coc) {
        this.keterangan_coc = keterangan_coc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
