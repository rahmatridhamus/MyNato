package my.mynato.rahmatridham.mynato.Model;

/**
 * Created by rahmatridham on 7/7/2017.
 */

public class ChoiModel {
    String id_aktivasi_choi, nama_choi, tanggal_mulai, tanggal_berakhir, keterangan;

    public ChoiModel(String id_aktivasi_choi, String nama_choi, String tanggal_mulai, String tanggal_berakhir, String keterangan) {
        this.id_aktivasi_choi = id_aktivasi_choi;
        this.nama_choi = nama_choi;
        this.tanggal_mulai = tanggal_mulai;
        this.tanggal_berakhir = tanggal_berakhir;
        this.keterangan = keterangan;
    }

    public String getId_aktivasi_choi() {
        return id_aktivasi_choi;
    }

    public void setId_aktivasi_choi(String id_aktivasi_choi) {
        this.id_aktivasi_choi = id_aktivasi_choi;
    }

    public String getNama_choi() {
        return nama_choi;
    }

    public void setNama_choi(String nama_choi) {
        this.nama_choi = nama_choi;
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
