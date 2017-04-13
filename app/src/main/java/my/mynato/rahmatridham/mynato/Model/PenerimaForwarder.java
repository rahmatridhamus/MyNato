package my.mynato.rahmatridham.mynato.Model;

/**
 * Created by rahmatridham on 3/20/2017.
 */

public class PenerimaForwarder {
    String kode_jabatan,nama_jabatan;
    boolean selected;

    public PenerimaForwarder(String kode_jabatan, String nama_jabatan, boolean selected) {
        this.kode_jabatan = kode_jabatan;
        this.nama_jabatan = nama_jabatan;
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getKode_jabatan() {
        return kode_jabatan;
    }

    public void setKode_jabatan(String kode_jabatan) {
        this.kode_jabatan = kode_jabatan;
    }

    public String getNama_jabatan() {
        return nama_jabatan;
    }

    public void setNama_jabatan(String nama_jabatan) {
        this.nama_jabatan = nama_jabatan;
    }
}
