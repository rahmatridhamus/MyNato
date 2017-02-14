package com.example.rahmatridham.mynato.Model;

/**
 * Created by rahmatridham on 2/14/2017.
 */

public class GroupCoc {
    String id,id_group_coc,nama_group_coc;

    public GroupCoc(String id, String id_group_coc, String nama_group_coc) {
        this.id = id;
        this.id_group_coc = id_group_coc;
        this.nama_group_coc = nama_group_coc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_group_coc() {
        return id_group_coc;
    }

    public void setId_group_coc(String id_group_coc) {
        this.id_group_coc = id_group_coc;
    }

    public String getNama_group_coc() {
        return nama_group_coc;
    }

    public void setNama_group_coc(String nama_group_coc) {
        this.nama_group_coc = nama_group_coc;
    }
}
