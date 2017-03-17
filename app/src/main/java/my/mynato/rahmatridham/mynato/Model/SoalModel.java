package my.mynato.rahmatridham.mynato.Model;

import java.util.ArrayList;

/**
 * Created by rahmatridham on 3/14/2017.
 */

public class SoalModel {
    String id_pertanyaan,pertanyaan;
    ArrayList<JawabanModel> jawabanModelArrayList;

    public SoalModel(String id_pertanyaan, String pertanyaan, ArrayList<JawabanModel> jawabanModelArrayList) {
        this.id_pertanyaan = id_pertanyaan;
        this.pertanyaan = pertanyaan;
        this.jawabanModelArrayList = jawabanModelArrayList;
    }

    public String getId_pertanyaan() {
        return id_pertanyaan;
    }

    public void setId_pertanyaan(String id_pertanyaan) {
        this.id_pertanyaan = id_pertanyaan;
    }

    public String getPertanyaan() {
        return pertanyaan;
    }

    public void setPertanyaan(String pertanyaan) {
        this.pertanyaan = pertanyaan;
    }

    public ArrayList<JawabanModel> getJawabanModelArrayList() {
        return jawabanModelArrayList;
    }

    public void setJawabanModelArrayList(ArrayList<JawabanModel> jawabanModelArrayList) {
        this.jawabanModelArrayList = jawabanModelArrayList;
    }
}
