package com.example.rahmatridham.mynato.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahmatridham on 2/21/2017.
 */

public class Thematik {
    String id_content_list,title;
    ArrayList<SubtitleThematik> subtitle;

    public Thematik(String id_content_list, String title, ArrayList<SubtitleThematik> subtitle) {
        this.id_content_list = id_content_list;
        this.title = title;
        this.subtitle = subtitle;
    }

    public List<String> getSubtit(){
        List<String> res = new ArrayList<String>();
        for (int i = 0; i < subtitle.size(); i++) {
            res.add(subtitle.get(i).getSub_title());
        }
        return res;
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

    public ArrayList<SubtitleThematik> getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(ArrayList<SubtitleThematik> subtitle) {
        this.subtitle = subtitle;
    }




}
