package my.mynato.rahmatridham.mynato.Model;

/**
 * Created by rahmatridham on 7/7/2017.
 */

public class ChoiDetailFormsModel {
    String id_formulir, title, nama_formulir, status;

    public ChoiDetailFormsModel(String id_formulir, String title, String nama_formulir, String status) {
        this.id_formulir = id_formulir;
        this.title = title;
        this.nama_formulir = nama_formulir;
        this.status = status;
    }

    public String getId_formulir() {
        return id_formulir;
    }

    public void setId_formulir(String id_formulir) {
        this.id_formulir = id_formulir;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNama_formulir() {
        return nama_formulir;
    }

    public void setNama_formulir(String nama_formulir) {
        this.nama_formulir = nama_formulir;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
