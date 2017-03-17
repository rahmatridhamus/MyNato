package my.mynato.rahmatridham.mynato.Model;

/**
 * Created by rahmatridham on 3/5/2017.
 */

public class SurveyModel {
    String id_survey,nama_ujian,tanggal_mulai,tanggal_berakhir,keterangan;

    public SurveyModel(String id_survey, String nama_ujian, String tanggal_mulai, String tanggal_berakhir, String keterangan) {
        this.id_survey = id_survey;
        this.nama_ujian = nama_ujian;
        this.tanggal_mulai = tanggal_mulai;
        this.tanggal_berakhir = tanggal_berakhir;
        this.keterangan = keterangan;
    }

    public String getId_survey() {
        return id_survey;
    }

    public void setId_survey(String id_survey) {
        this.id_survey = id_survey;
    }

    public String getNama_ujian() {
        return nama_ujian;
    }

    public void setNama_ujian(String nama_ujian) {
        this.nama_ujian = nama_ujian;
    }

    public String getTanggal_mulai() {
        return tanggal_mulai;
    }

    public void setTanggal_mulai(String tanggal_mulai) {
        this.tanggal_mulai = tanggal_mulai;
    }

    public String getTanggal_berakhir() {
        return tanggal_berakhir;
    }

    public void setTanggal_berakhir(String tanggal_berakhir) {
        this.tanggal_berakhir = tanggal_berakhir;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}
