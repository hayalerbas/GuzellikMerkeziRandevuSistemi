package application;

public class CalisanModel {

    private int id;
    private String adSoyad;
    private String uzmanlikAlani;
    private String telefon;
    private String email;
    private String durum;

    public CalisanModel(int id, String adSoyad, String uzmanlikAlani,
                        String telefon, String email, String durum) {

        this.id = id;
        this.adSoyad = adSoyad;
        this.uzmanlikAlani = uzmanlikAlani;
        this.telefon = telefon;
        this.email = email;
        this.durum = durum;
    }

    public int getId() {
        return id;
    }

    public String getAdSoyad() {
        return adSoyad;
    }

    public String getUzmanlikAlani() {
        return uzmanlikAlani;
    }

    public String getTelefon() {
        return telefon;
    }

    public String getEmail() {
        return email;
    }

    public String getDurum() {
        return durum;
    }
}