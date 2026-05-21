package application;

public class MusteriModel {

    private int id;
    private String adSoyad;
    private String email;
    private String telefon;
    private String durum;
    private String kayitTarihi;

    public MusteriModel(int id, String adSoyad,
                         String email, String telefon,
                         String durum, String kayitTarihi) {

        this.id = id;
        this.adSoyad = adSoyad;
        this.email = email;
        this.telefon = telefon;
        this.durum = durum;
        this.kayitTarihi = kayitTarihi;
    }

    public int getId() {
        return id;
    }

    public String getAdSoyad() {
        return adSoyad;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefon() {
        return telefon;
    }

    public String getDurum() {
        return durum;
    }

    public String getKayitTarihi() {
        return kayitTarihi;
    }
}