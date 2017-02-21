package com.example.rahmatridham.mynato.Model;

/**
 * Created by rahmatridham on 2/21/2017.
 */

public class CeritaMotivasi {
    String id,title,file,keterangan;

    public CeritaMotivasi(String id, String title, String file, String keterangan) {
        this.id = id;
        this.title = title;
        this.file = file;
        this.keterangan = keterangan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}
