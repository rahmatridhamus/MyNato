package my.mynato.rahmatridham.mynato.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahmatridham on 2/21/2017.
 */

public class DoAndDont {
    String id_do_and_dont, title;
    ArrayList<SubDoDont> subDoDonts;

    public DoAndDont(String id_do_and_dont, String title, ArrayList<SubDoDont> subDoDonts) {
        this.id_do_and_dont = id_do_and_dont;
        this.title = title;
        this.subDoDonts = subDoDonts;
    }

    public List<String> getSubtit(){
        List<String> res = new ArrayList<String>();
        for (int i = 0; i < subDoDonts.size(); i++) {
            res.add(subDoDonts.get(i).getSub_title());
        }
        return res;
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

    public ArrayList<SubDoDont> getSubDoDonts() {
        return subDoDonts;
    }

    public void setSubDoDonts(ArrayList<SubDoDont> subDoDonts) {
        this.subDoDonts = subDoDonts;
    }

    @Override
    public String toString() {
        return "DoAndDont{" +
                "id_do_and_dont='" + id_do_and_dont + '\'' +
                ", title='" + title + '\'' +
                ", subDoDonts=" + subDoDonts.size() +
                '}';
    }
}
