package com.example.rahmatridham.mynato.Model;

/**
 * Created by rahmatridham on 2/23/2017.
 */

public class SubDoDont {
    String id_sub_do_and_dont,sub_title,dost,dont;

    public SubDoDont(String id_sub_do_and_dont, String sub_title, String dost, String dont) {
        this.id_sub_do_and_dont = id_sub_do_and_dont;
        this.sub_title = sub_title;
        this.dost = dost;
        this.dont = dont;
    }

    public String getId_sub_do_and_dont() {
        return id_sub_do_and_dont;
    }

    public void setId_sub_do_and_dont(String id_sub_do_and_dont) {
        this.id_sub_do_and_dont = id_sub_do_and_dont;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public String getDost() {
        return dost;
    }

    public void setDost(String dost) {
        this.dost = dost;
    }

    public String getDont() {
        return dont;
    }

    public void setDont(String dont) {
        this.dont = dont;
    }
}
