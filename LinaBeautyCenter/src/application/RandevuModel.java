package application;

public class RandevuModel {

    private int id;
    private String adSoyad;
    private String telefon;
    private String hizmet;
    private String uzman;
    private String tarih;
    private String saat;
    private String notunuz;
    private String durum;

    public RandevuModel(int id, String adSoyad, String telefon,
                        String hizmet, String uzman, String tarih,
                        String saat, String notunuz, String durum) {

        this.id = id;
        this.adSoyad = adSoyad;
        this.telefon = telefon;
        this.hizmet = hizmet;
        this.uzman = uzman;
        this.tarih = tarih;
        this.saat = saat;
        this.notunuz = notunuz;
        this.durum = durum;
    }

    public int getId() {
        return id;
    }

    public String getAdSoyad() {
        return adSoyad;
    }

    public String getTelefon() {
        return telefon;
    }

    public String getHizmet() {
        return hizmet;
    }

    public String getUzman() {
        return uzman;
    }

    public String getTarih() {
        return tarih;
    }

    public String getSaat() {
        return saat;
    }

    public String getNotunuz() {
        return notunuz;
    }

    public String getDurum() {
        return durum;
    }
}