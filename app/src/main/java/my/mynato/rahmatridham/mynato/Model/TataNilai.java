package my.mynato.rahmatridham.mynato.Model;

/**
 * Created by rahmatridham on 2/21/2017.
 */

public class TataNilai {
    String id_tata_nilai, title, content;

    public TataNilai(String id_tata_nilai, String title, String content) {
        this.id_tata_nilai = id_tata_nilai;
        this.title = title;
        this.content = content;
    }

    public String getId_tata_nilai() {
        return id_tata_nilai;
    }

    public void setId_tata_nilai(String id_tata_nilai) {
        this.id_tata_nilai = id_tata_nilai;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
