package my.mynato.rahmatridham.mynato.Model;

/**
 * Created by rahmatridham on 2/24/2017.
 */

public class PakKadirModel {
    String id_pak_kadir, title, content, sending_date, received_date, reading_time, status;

    public PakKadirModel(String id_pak_kadir, String title, String content, String sending_date, String received_date, String reading_time, String status) {
        this.id_pak_kadir = id_pak_kadir;
        this.title = title;
        this.content = content;
        this.sending_date = sending_date;
        this.received_date = received_date;
        this.reading_time = reading_time;
        this.status = status;
    }

    public String getId_pak_kadir() {
        return id_pak_kadir;
    }

    public void setId_pak_kadir(String id_pak_kadir) {
        this.id_pak_kadir = id_pak_kadir;
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

    public String getSending_date() {
        return sending_date;
    }

    public void setSending_date(String sending_date) {
        this.sending_date = sending_date;
    }

    public String getReceived_date() {
        return received_date;
    }

    public void setReceived_date(String received_date) {
        this.received_date = received_date;
    }

    public String getReading_time() {
        return reading_time;
    }

    public void setReading_time(String reading_time) {
        this.reading_time = reading_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
