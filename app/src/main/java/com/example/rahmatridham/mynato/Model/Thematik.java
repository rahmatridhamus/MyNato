package com.example.rahmatridham.mynato.Model;

import java.util.ArrayList;

/**
 * Created by rahmatridham on 2/21/2017.
 */

public class Thematik {
    String id_content_list,title;
    ArrayList<Subtitle> subtitle;

    public Thematik(String id_content_list, String title, ArrayList<Subtitle> subtitle) {
        this.id_content_list = id_content_list;
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getId_content_list() {
        return id_content_list;
    }

    public void setId_content_list(String id_content_list) {
        this.id_content_list = id_content_list;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Subtitle> getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(ArrayList<Subtitle> subtitle) {
        this.subtitle = subtitle;
    }




}
