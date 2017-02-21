package com.example.rahmatridham.mynato.Model;

/**
 * Created by rahmatridham on 2/21/2017.
 */

public class DoAndDont {
    String id_do_and_dont,title,sub_title,dost,dont;

    public DoAndDont(String id_do_and_dont, String title, String sub_title, String dost, String dont) {
        this.id_do_and_dont = id_do_and_dont;
        this.title = title;
        this.sub_title = sub_title;
        this.dost = dost;
        this.dont = dont;
    }

    public String getId_do_and_dont() {
        return id_do_and_dont;
    }

    public void setId_do_and_dont(String id_do_and_dont) {
        this.id_do_and_dont = id_do_and_dont;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
