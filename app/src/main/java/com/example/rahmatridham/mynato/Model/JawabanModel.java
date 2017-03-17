package com.example.rahmatridham.mynato.Model;

/**
 * Created by rahmatridham on 3/14/2017.
 */

public class JawabanModel {
    String id_jawaban, nama_jawaban;

    public JawabanModel(String id_jawaban, String nama_jawaban) {
        this.id_jawaban = id_jawaban;
        this.nama_jawaban = nama_jawaban;
    }

    public String getId_jawaban() {
        return id_jawaban;
    }

    public void setId_jawaban(String id_jawaban) {
        this.id_jawaban = id_jawaban;
    }

    public String getNama_jawaban() {
        return nama_jawaban;
    }

    public void setNama_jawaban(String nama_jawaban) {
        this.nama_jawaban = nama_jawaban;
    }
}
